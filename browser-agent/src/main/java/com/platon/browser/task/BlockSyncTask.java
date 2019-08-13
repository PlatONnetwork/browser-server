package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.BlockInfo;
import com.platon.browser.dto.TransactionInfo;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.BlockChainResult;
import com.platon.browser.engine.ProposalExecuteResult;
import com.platon.browser.engine.StakingExecuteResult;
import com.platon.browser.exception.BusinessException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.PlatonGetCode;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 09:14
 * @Description:
 */
@Component
public class BlockSyncTask {

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    private static Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);

    private static ExecutorService THREAD_POOL;

    private static ExecutorService TX_THREAD_POOL;

    @Autowired
    private BlockChain blockChain;

    @Autowired
    private PlatonClient client;

    // 已采集入库的最高块
    private long commitBlockNumber=0;

    // 每一批次采集区块的数量
    @Value("${platon.web3j.collect.batch-size}")
    private int collectBatchSize;

    @Data
    class Result{
        // 并发采集的块信息，无序
        public Map<Long,BlockInfo> concurrentBlockMap = new ConcurrentHashMap<>();
        // 由于异常而未采集的区块号列表
        private Set<BigInteger> retryNumbers = new CopyOnWriteArraySet<>();
        // 已排序的区块信息列表
        private List<BlockInfo> sortedBlocks = new LinkedList<>();
        public List<BlockInfo> getSortedBlocks(){
            if(sortedBlocks.size()==0){
                sortedBlocks.addAll(concurrentBlockMap.values());
                Collections.sort(sortedBlocks,(c1,c2)->{
                    if (c1.getNumber().compareTo(c2.getNumber())>0) return 1;
                    if (c1.getNumber().compareTo(c2.getNumber())<0) return -1;
                    return 0;
                });
            }
            return sortedBlocks;
        }
    }

    /**
     * 初始化已有业务数据
     */
    @PostConstruct
    public void init(){
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        TX_THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize * 2);
        Long maxBlockNumber = customBlockMapper.selectMaxBlockNumber();
        if(maxBlockNumber!=null&&maxBlockNumber>0) commitBlockNumber = maxBlockNumber;
    }

    public void start() throws InterruptedException {

        while (true){
            // 构造连续的待采区块号列表
            List<BigInteger> blockNumbers = new ArrayList<>();
            for (long blockNumber=commitBlockNumber+1;blockNumber<=(commitBlockNumber+collectBatchSize);blockNumber++){
                blockNumbers.add(BigInteger.valueOf(blockNumber));
            }

            // 并行采集区块
            Result collectedResult = new Result();
            getBlockAndTransaction(blockNumbers,collectedResult);
            List<BlockInfo> blocks = collectedResult.getSortedBlocks();
            // 对区块和交易做分析
            analyzeBlockAndTransaction(blocks);

            // 调用BlockChain实例，分析质押、提案相关业务数据
            BlockChainResult bizData = new BlockChainResult();
            ProposalExecuteResult perSummary = bizData.getProposalExecuteResult();
            StakingExecuteResult serSummary = bizData.getStakingExecuteResult();
            blocks.forEach(block->{
                blockChain.execute(block);
                BlockChainResult bcr = blockChain.exportResult();
                ProposalExecuteResult per = bcr.getProposalExecuteResult();
                StakingExecuteResult ser = bcr.getStakingExecuteResult();

                perSummary.getAddVotes().addAll(per.getAddVotes());
                perSummary.getAddProposals().addAll(per.getAddProposals());
                perSummary.getUpdateProposals().addAll(per.getUpdateProposals());

                serSummary.getAddDelegations().addAll(ser.getAddDelegations());
                serSummary.getAddNodeOpts().addAll(ser.getAddNodeOpts());
                serSummary.getAddNodes().addAll(ser.getAddNodes());
                serSummary.getAddSlash().addAll(ser.getAddSlash());


                // 清除blockChain实例状态，防止影响下一次的循环
                blockChain.commitResult();
            });

            try {
                // 入库失败，立即停止，防止采集后续更高的区块号，导致不连续区块号出现
                batchSaveResult(blocks,bizData);
            }catch (BusinessException e){
                break;
            }

            if(blocks.size()>0) commitBlockNumber=blocks.get(blocks.size()-1).getNumber();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 并行采集区块及交易，并转换为数据库结构
     * @param blockNumbers 批量采集的区块号
     * @return
     */
    private void getBlockAndTransaction(List<BigInteger> blockNumbers,Result result){

        result.retryNumbers.clear();

        // 并行批量采集区块
        CountDownLatch latch = new CountDownLatch(blockNumbers.size());
        blockNumbers.forEach(blockNumber->
            THREAD_POOL.submit(()->{
                try {
                    Web3j web3j = client.getWeb3j();
                    PlatonBlock.Block initData = web3j.platonGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber),true).send().getBlock();
                    if(initData!=null) {
                        try{
                            BlockInfo block = new BlockInfo(initData);
                            try{
                                result.concurrentBlockMap.put(blockNumber.longValue(),block);
                            }catch (Exception ex){
                                logger.debug("Add BlockInfo Exception!");
                                throw ex;
                            }
                        }catch (Exception ex){
                            logger.debug("New BlockInfo Exception!");
                            throw ex;
                        }
                    }
                } catch (Exception e) {
                    result.retryNumbers.add(blockNumber);
                }finally {
                    latch.countDown();
                }
            })
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(result.concurrentBlockMap.size()>0){
            // 查看已采块是否连续，把缺失的区块号放入重试列表
            Set<Long> collectedNumbers = result.concurrentBlockMap.keySet();
            List<Long> collectedList = new ArrayList<>(collectedNumbers);
            Collections.sort(collectedList,(c1,c2)->{
                if(c1>c2) return 1;
                if(c1<c2) return -1;
                return 0;
            });
            long start = collectedList.get(0),end = collectedList.get(collectedList.size()-1);
            for (long i=start;i<end;i++){
                if(!collectedList.contains(i)) result.retryNumbers.add(BigInteger.valueOf(i));
            }
        }

        if(result.retryNumbers.size()>0){
            logger.debug("区块重试列表：{}",result.retryNumbers);
            getBlockAndTransaction(new ArrayList<>(result.retryNumbers),result);
        }
    }

    /**
     * 并行分析区块
     */
    private void analyzeBlockAndTransaction(List<BlockInfo> blocks){

        // 对需要复杂分析的区块或交易信息，开启并行处理
        blocks.forEach(b -> {
            List<TransactionInfo> txList = b.getTransactionList();
            txList.forEach(tx ->{
                TX_THREAD_POOL.submit(() -> {
                    updateTransactionInfo(tx);
                });
            });
        });


        // 交易信息统计
        class Stat {
            int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            BigDecimal txFee = BigDecimal.ZERO;
        }

        blocks.forEach(block->{
            Stat stat = new Stat();
            block.getTransactionList().forEach(ti->{
                switch (ti.getTypeEnum()){
                    case TRANSFER:
                        stat.transferQty++;
                        break;
                    case CREATEPROPOSALPARAMETER:// 创建参数提案
                    case CREATEPROPOSALTEXT:// 创建文本提案
                    case CREATEPROPOSALUPGRADE:// 创建升级提案
                    case DECLAREVERSION:// 版本声明
                    case VOTINGPROPOSAL:// 提案投票
                        stat.proposalQty++;
                        break;
                    case DELEGATE:// 发起委托
                    case UNDELEGATE:// 撤销委托
                        stat.delegateQty++;
                        break;
                    case INCREASESTAKING:// 增加自有质押
                    case CREATEVALIDATOR:// 创建验证人
                    case EXITVALIDATOR:// 退出验证人
                    case REPORTVALIDATOR:// 举报验证人
                    case EDITVALIDATOR:// 编辑验证人
                        stat.stakingQty++;
                        break;
                }
                // 累加交易手续费
                stat.txFee = stat.txFee.add(new BigDecimal(ti.getActualTxCost()));
                // 累加交易gasLimit
                stat.txGasLimit = stat.txGasLimit+Integer.valueOf(ti.getGasLimit());
            });
            block.setStatDelegateQty(stat.delegateQty);
            block.setStatProposalQty(stat.proposalQty);
            block.setStatStakingQty(stat.stakingQty);
            block.setStatTransferQty(stat.transferQty);
            block.setStatTxGasLimit(String.valueOf(stat.txGasLimit));
            block.setStatTxFee(stat.txFee.toString());
        });




    }

    /**
     * 分析区块获取code&交易回执
     */
    private TransactionInfo  updateTransactionInfo(TransactionInfo tx){
        try {
            PlatonGetTransactionReceipt platonGetTransactionReceipt = client.getWeb3j().platonGetTransactionReceipt(tx.getHash()).send();
            Optional<TransactionReceipt> receipts = platonGetTransactionReceipt.getTransactionReceipt();
            PlatonGetCode platonGetCode = client.getWeb3j().platonGetCode(tx.getTo(), DefaultBlockParameterName.LATEST).send();

        }catch (IOException e){

        }
        return tx;
    }

    @Transactional
    public void batchSaveResult(List<BlockInfo> basicData,BlockChainResult bizData){
        // 串行批量入库
        try{
            List<Block> blocks = new ArrayList<>();
            List<TransactionWithBLOBs> transactions = new ArrayList<>();
            basicData.forEach(block->{
                blocks.add(block);
                transactions.addAll(block.getTransactionList());
            });
            // 批量入库区块
            if (blocks.size()>0) {
                blockMapper.batchInsert(blocks);
            }
            // 批量入库交易
            if(transactions.size()>0) {
                transactionMapper.batchInsert(transactions);
            }
        }catch (Exception e){
            logger.debug("入库失败！原因："+e.getMessage());
            throw new BusinessException("入库失败！原因："+e.getMessage());
        }
    }
}
