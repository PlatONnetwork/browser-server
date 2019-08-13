package com.platon.browser.task;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.BlockMapper;
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
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.PlatonGetCode;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    /**
     * 初始化已有业务数据
     */
    @PostConstruct
    public void init(){
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        TX_THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize * 2);
    }

    public void start() throws InterruptedException {

        while (true){
            // 构造连续的待采区块号列表
            List<BigInteger> blockNumbers = new ArrayList<>();
            for (long blockNumber=commitBlockNumber+1;blockNumber<commitBlockNumber+collectBatchSize;blockNumber++){
                blockNumbers.add(BigInteger.valueOf(blockNumber));
            }

            // 并行采集区块
            Result collectedResult = getBlockAndTransaction(blockNumbers);
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


                // 清楚blockChain实例状态，防止影响下一次的循环
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

    @Data
    class Result{
        // 并发采集的块信息，无序
        public Set<BlockInfo> concurrentBlocks = new CopyOnWriteArraySet<>();
        // 采集过程中的错误信息
        List<Error> errors = new CopyOnWriteArrayList<>();
        // 已采集的区块号列表
        List<Long> collectedNumbers = new CopyOnWriteArrayList<>();
        // 由于异常而未采集的区块号列表
        private Set<BigInteger> exceptionNumbers = new CopyOnWriteArraySet<>();
        // 已排序的区块信息列表
        private List<BlockInfo> sortedBlocks = new LinkedList<>();
        public List<BlockInfo> getSortedBlocks(){
            if(sortedBlocks.size()==0){
                sortedBlocks.addAll(concurrentBlocks);
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
     * 并行采集区块及交易，并转换为数据库结构
     * @param blockNumbers 批量采集的区块号
     * @return
     */
    private Result getBlockAndTransaction(List<BigInteger> blockNumbers){

        Result result = new Result();

        // 并行批量采集区块
        CountDownLatch latch = new CountDownLatch(blockNumbers.size());
        blockNumbers.forEach(blockNumber->
            THREAD_POOL.submit(()->{
                try {
                    // 如果区块号已搜集，则返回
                    if(result.collectedNumbers.contains(blockNumber.longValue())) return;
                    PlatonBlock.Block initData = client.getWeb3j().platonGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber),true).send().getBlock();
                    if(initData!=null) {
                        result.concurrentBlocks.add(new BlockInfo(initData));
                        result.collectedNumbers.add(blockNumber.longValue());
                    }
                } catch (Exception e) {
                    result.exceptionNumbers.add(blockNumber);
                    logger.error("已放入重试列表！",blockNumber,e.getMessage());
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


        // 如果有异常，则进行重试，直到采集完本批次所有在链上存在的区块
        List<BigInteger> retryNumbers = new ArrayList<>();

        if(result.collectedNumbers.size()>0){
            // 检测由于各节点区块同步不及时导致的区块缺失
            Collections.sort(result.collectedNumbers,(c1,c2)->{
                if (c1.compareTo(c2)>0) return 1;
                if (c1.compareTo(c2)<0) return -1;
                return 0;
            });

            long start = result.collectedNumbers.get(0);
            long end = result.collectedNumbers.get(result.collectedNumbers.size()-1);

            Set<BigInteger> uncollectedNumbers = new HashSet<>();
            for (long i=start;i<=end;i++){
                if(!result.collectedNumbers.contains(i)) uncollectedNumbers.add(BigInteger.valueOf(i));
            }
            if(uncollectedNumbers.size()>0){
                logger.info("由于同步不及时未采集到的区块：{}",uncollectedNumbers);
                retryNumbers.addAll(uncollectedNumbers);
            }
        }

        if(result.exceptionNumbers.size()>0){
            logger.info("由于Web3j异常未采集到的区块：{}",result.exceptionNumbers);
            retryNumbers.addAll(result.exceptionNumbers);
        }

        if(retryNumbers.size()>0){
            logger.info("区块重试列表：{}",retryNumbers);
            Result retryResult = getBlockAndTransaction(retryNumbers);
            // 把结果汇总到本递归结果中
            result.concurrentBlocks.addAll(retryResult.concurrentBlocks);
        }

        return result;
    }

    /**
     * 并行分析区块
     */
    private void analyzeBlockAndTransaction(List<BlockInfo> blocks){

        // 对需要复杂分析的区块或交易信息，开启并行处理
        blocks.forEach(b -> {
            List<TransactionInfo> txList = b.getTransactions();
            txList.forEach(tx ->{
                TX_THREAD_POOL.submit(() -> {
                    updateTransactionInfo(tx);
                });
            });
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
            tx.updateTransactionInfo(receipts.get(),platonGetCode.getCode());
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
                transactions.addAll(block.getTransactions());
            });
            // 批量入库区块
            if (blocks.size()>0) blockMapper.batchInsert(blocks);
            // 批量入库交易
            if(transactions.size()>0) transactionMapper.batchInsert(transactions);
        }catch (Exception e){
            throw new BusinessException("入库失败！原因："+e.getMessage());
        }
    }
}
