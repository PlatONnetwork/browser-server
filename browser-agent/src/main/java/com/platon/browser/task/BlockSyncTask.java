package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dto.BlockBean;
import com.platon.browser.dto.NodeBean;
import com.platon.browser.dto.StakingBean;
import com.platon.browser.dto.TransactionBean;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.BlockChainResult;
import com.platon.browser.engine.ProposalExecuteResult;
import com.platon.browser.engine.StakingExecuteResult;
import com.platon.browser.enums.InnerContractAddEnum;
import com.platon.browser.enums.ReceiveTypeEnum;
import com.platon.browser.enums.TxTypeEnum;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.service.DbService;
import com.platon.browser.util.TxParamResolver;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

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
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private DbService service;

    private static Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);

    private static ExecutorService THREAD_POOL;

    private static ExecutorService TX_THREAD_POOL;

    @Autowired
    private BlockChain blockChain;

    @Autowired
    private PlatonClient client;

    // 已采集入库的最高块
    private long commitBlockNumber = 0;

    // 每一批次采集区块的数量
    @Value("${platon.web3j.collect.batch-size}")
    private int collectBatchSize;

    @Data
    class Result {
        // 并发采集的块信息，无序
        public Map <Long, BlockBean> concurrentBlockMap = new ConcurrentHashMap <>();
        // 由于异常而未采集的区块号列表
        private Set <BigInteger> retryNumbers = new CopyOnWriteArraySet <>();
        // 已排序的区块信息列表
        private List <BlockBean> sortedBlocks = new LinkedList <>();

        public List <BlockBean> getSortedBlocks () {
            if (sortedBlocks.size() == 0) {
                sortedBlocks.addAll(concurrentBlockMap.values());
                Collections.sort(sortedBlocks, ( c1, c2 ) -> {
                    if (c1.getNumber().compareTo(c2.getNumber()) > 0) return 1;
                    if (c1.getNumber().compareTo(c2.getNumber()) < 0) return -1;
                    return 0;
                });
            }
            return sortedBlocks;
        }
    }

    /**
     * 初始化已有业务数据
     */
    public void init () {
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        TX_THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize * 2);
        Long maxBlockNumber = customBlockMapper.selectMaxBlockNumber();
        if (maxBlockNumber != null && maxBlockNumber > 0) commitBlockNumber = maxBlockNumber;

        // 结算周期验证人
        if(maxBlockNumber==null){
            // 如果库里区块为空，则：
            try {
                // 根据区块号0查询共识周期验证人，以便对结算周期验证人设置共识标识
                BaseResponse<List<Node>> validators = client.getHistoryValidatorList(BigInteger.ONE);
                if(!validators.isStatusOk()){
                    logger.debug("通过区块号[{}]查询历史共识周期验证人列表为空:{}",BigInteger.ONE,validators.errMsg);
                    logger.debug("查询实时共识周期验证人列表...");
                    validators = client.getNodeContract().getValidatorList().send();
                }
                Map<String,Node> validatorMap = new HashMap<>();
                if(validators.isStatusOk()) validators.data.forEach(node->validatorMap.put(HexTool.prefix(node.getNodeId()),node));

                // 根据区块号0查询结算周期验证人列表并入库
                BaseResponse<List<Node>> verifiers = client.getHistoryVerifierList(BigInteger.ONE);
                if(!verifiers.isStatusOk()){
                    logger.debug("通过区块号[{}]查询历史结算周期验证人列表为空:{}",BigInteger.ONE,verifiers.errMsg);
                    logger.debug("查询实时结算周期验证人列表...");
                    verifiers = client.getNodeContract().getVerifierList().send();
                }
                if(verifiers.isStatusOk()) {
                    BlockChainResult bcr = new BlockChainResult();
                    verifiers.data.stream().filter(Objects::nonNull).forEach(verifier->{
                        NodeBean node = new NodeBean();
                        node.initWithNode(verifier);
                        node.setIsRecommend(1);
                        bcr.getStakingExecuteResult().getAddNodes().add(node);

                        StakingBean stakingBean = new StakingBean();
                        stakingBean.initWithNode(verifier);
                        stakingBean.setStakingIcon("");
                        stakingBean.setIsInit(1);
                        stakingBean.setIsSetting(1);
                        // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
                        if(validatorMap.get(node.getNodeId())!=null) stakingBean.setIsConsensus(1);

                        bcr.getStakingExecuteResult().getAddStakings().add(stakingBean);
                    });
                    service.insertOrUpdateChainInfo(Collections.EMPTY_LIST,bcr);
                }
                // 通知质押引擎重新初始化节点缓存
                blockChain.getStakingExecute().loadNodes();
            } catch (Exception e) {
                logger.error("查询内置初始验证人列表失败,原因：{}",e.getMessage());
            }
        }
    }

    public void start () throws InterruptedException {

        while (true) {
            // 构造连续的待采区块号列表
            List <BigInteger> blockNumbers = new ArrayList <>();
            for (long blockNumber = commitBlockNumber + 1; blockNumber <= (commitBlockNumber + collectBatchSize); blockNumber++) {
                blockNumbers.add(BigInteger.valueOf(blockNumber));
            }

            // 并行采集区块
            Result collectedResult = new Result();
            getBlockAndTransaction(blockNumbers, collectedResult);
            List <BlockBean> blocks = collectedResult.getSortedBlocks();

            // 采集不到区块则则暂停1秒, 结束本次循环
            if(blocks.size()==0) {
                TimeUnit.SECONDS.sleep(1);
                continue;
            }

            // 对区块和交易做分析
            analyzeBlockAndTransaction(blocks);
            // 调用BlockChain实例，分析质押、提案相关业务数据
            BlockChainResult bizData = new BlockChainResult();
            ProposalExecuteResult perSummary = bizData.getProposalExecuteResult();
            StakingExecuteResult serSummary = bizData.getStakingExecuteResult();
            blocks.forEach(block -> {
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
                batchSaveResult(blocks, bizData);
            } catch (BusinessException e) {
                break;
            }

            commitBlockNumber = blocks.get(blocks.size() - 1).getNumber();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 并行采集区块及交易，并转换为数据库结构
     *
     * @param blockNumbers 批量采集的区块号
     * @return
     */
    private void getBlockAndTransaction ( List <BigInteger> blockNumbers, Result result ) {

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
                            BlockBean block = new BlockBean();
                            block.init(initData);
                            result.concurrentBlockMap.put(blockNumber.longValue(),block);
                        }catch (Exception ex){
                            logger.debug("初始化区块信息异常, 原因: {}", ex.getMessage());
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

        if (result.concurrentBlockMap.size() > 0) {
            // 查看已采块是否连续，把缺失的区块号放入重试列表
            Set <Long> collectedNumbers = result.concurrentBlockMap.keySet();
            List <Long> collectedList = new ArrayList <>(collectedNumbers);
            Collections.sort(collectedList, ( c1, c2 ) -> {
                if (c1 > c2) return 1;
                if (c1 < c2) return -1;
                return 0;
            });
            long start = collectedList.get(0), end = collectedList.get(collectedList.size() - 1);
            for (long i = start; i < end; i++) {
                if (!collectedList.contains(i)) result.retryNumbers.add(BigInteger.valueOf(i));
            }
        }

        if (result.retryNumbers.size() > 0) {
            logger.debug("区块重试列表：{}", result.retryNumbers);
            getBlockAndTransaction(new ArrayList <>(result.retryNumbers), result);
        }
    }

    /**
     * 并行分析区块
     */
    private void analyzeBlockAndTransaction ( List <BlockBean> blocks ) {
        // 对需要复杂分析的区块或交易信息，开启并行处理
        blocks.forEach(b -> {
            List <TransactionBean> txList = b.getTransactionList();
            CountDownLatch latch = new CountDownLatch(txList.size());
            txList.forEach(tx ->
                    TX_THREAD_POOL.submit(() -> {
                        try {
                            updateTransactionInfo(tx);
                        } catch (Exception e) {
                            logger.error("更新交易信息错误：{}", e.getMessage());
                        } finally {
                            latch.countDown();
                        }
                    })
            );
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 汇总区块相关的统计信息
        class Stat {
            int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            BigDecimal txFee = BigDecimal.ZERO;
        }
        blocks.forEach(block->{
            Stat stat = new Stat();
            block.getTransactionList().forEach(ti->{
                switch (ti.getTypeEnum()){
                    case TRANSFER: // 转账交易数总和
                        stat.transferQty++;
                        break;
                    case CREATEPROPOSALPARAMETER:// 创建参数提案
                    case CREATEPROPOSALTEXT:// 创建文本提案
                    case CREATEPROPOSALUPGRADE:// 创建升级提案
                    case DECLAREVERSION:// 版本声明
                    case VOTINGPROPOSAL:// 提案投票
                        stat.proposalQty++; // 提案交易数总和
                        break;
                    case DELEGATE:// 发起委托
                    case UNDELEGATE:// 撤销委托
                        stat.delegateQty++; // 委托交易数总和
                        break;
                    case INCREASESTAKING:// 增加自有质押
                    case CREATEVALIDATOR:// 创建验证人
                    case EXITVALIDATOR:// 退出验证人
                    case REPORTVALIDATOR:// 举报验证人
                    case EDITVALIDATOR:// 编辑验证人
                        stat.stakingQty++; // 质押交易数总和
                        break;
                }
                // 累加当前区块内所有交易的手续费
                stat.txFee = stat.txFee.add(new BigDecimal(ti.getActualTxCost()));
                // 累加当前区块内所有交易的GasLimit
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
    private TransactionBean updateTransactionInfo(TransactionBean tx) throws IOException, BeanCreateOrUpdateException {
        try {
            // 查询交易回执
            PlatonGetTransactionReceipt result = client.getWeb3j().platonGetTransactionReceipt(tx.getHash()).send();
            Optional<TransactionReceipt> receipt = result.getTransactionReceipt();
            // 如果交易回执存在，则更新交易中与回执相关的信息
            if(receipt.isPresent()) tx.update(receipt.get());
        }catch (IOException e){
            logger.error("查询交易[hash={}]的回执出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }catch (BeanCreateOrUpdateException e){
            logger.error("更新交易[hash={}]的回执相关信息出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }

        // 解析交易参数，补充交易中与交易参数相关的信息
        try {
            TxParamResolver.Result txParams = TxParamResolver.analysis(tx.getInput());
            tx.setTypeEnum(txParams.getTxTypeEnum());
            tx.setTxInfo(JSON.toJSONString(txParams.getParam()));
            tx.setTxType(String.valueOf(txParams.getTxTypeEnum().code));
            tx.setReceiveType(ReceiveTypeEnum.CONTRACT.name().toLowerCase());
            if(null != tx.getValue() && ! InnerContractAddEnum.innerContractList.contains(tx.getTo())){
                tx.setTxType(String.valueOf(TxTypeEnum.TRANSFER.code));
                tx.setReceiveType(ReceiveTypeEnum.ACCOUNT.name().toLowerCase());
            }
        }catch (Exception e){
            logger.error("交易[hash={}]的参数解析出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }

        return tx;
    }

    @Transactional
    public void batchSaveResult(List<BlockBean> basicData, BlockChainResult bizData){
        // 串行批量入库
        try{
            service.insertOrUpdateChainInfo(basicData,bizData);
        }catch (Exception e){
            logger.error("入库失败！原因："+e.getMessage());
            throw new BusinessException("入库失败！原因："+e.getMessage());
        }
    }
}
