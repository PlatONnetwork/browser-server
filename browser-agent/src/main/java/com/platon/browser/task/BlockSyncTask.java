package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.result.BlockChainResult;
import com.platon.browser.enums.InnerContractAddEnum;
import com.platon.browser.enums.ReceiveTypeEnum;
import com.platon.browser.enums.TxTypeEnum;
import com.platon.browser.exception.*;
import com.platon.browser.service.DbService;
import com.platon.browser.util.TxParamResolver;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.platon.contracts.StakingContract;
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
        public Map <Long, CustomBlock> concurrentBlockMap = new ConcurrentHashMap <>();
        // 由于异常而未采集的区块号列表
        private Set <BigInteger> retryNumbers = new CopyOnWriteArraySet <>();
        // 已排序的区块信息列表
        private List <CustomBlock> sortedBlocks = new LinkedList <>();

        public List <CustomBlock> getSortedBlocks () {
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
    public void init () throws CandidateException {
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        TX_THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize * 2);
        Long maxBlockNumber = customBlockMapper.selectMaxBlockNumber();
        if (maxBlockNumber != null && maxBlockNumber > 0) commitBlockNumber = maxBlockNumber;

        /**
         * 从第一块同步的时候，结算周期验证人和共识周期验证人是链上内置的
         * 查询内置共识周期验证人初始化blockChain的curValidator属性
         * 查询内置结算周期验证人初始化blockChain的curVerifier属性
          */
        if(maxBlockNumber==null){
            // 如果库里区块为空，则：
            try {
                // 根据区块号0查询共识周期验证人，以便对结算周期验证人设置共识标识
                BaseResponse<List<Node>> validators = client.getHistoryValidatorList(BigInteger.ZERO);
                if(!validators.isStatusOk()){
                    logger.debug("通过区块号[{}]查询历史共识周期验证人列表为空:{}",BigInteger.ONE,validators.errMsg);
                    logger.debug("查询实时共识周期验证人列表...");
                    validators = client.getNodeContract().getValidatorList().send();
                }
                // 查询内置共识周期验证人初始化blockChain的curValidator属性
                if(validators.isStatusOk()) validators.data.forEach(node->blockChain.getCurValidator().put(HexTool.prefix(node.getNodeId()),node));

                // 根据区块号0查询结算周期验证人列表并入库
                BaseResponse<List<Node>> verifiers = client.getHistoryVerifierList(BigInteger.ZERO);
                if(!verifiers.isStatusOk()){
                    logger.debug("通过区块号[{}]查询历史结算周期验证人列表为空:{}",BigInteger.ONE,verifiers.errMsg);
                    logger.debug("查询实时结算周期验证人列表...");
                    verifiers = client.getNodeContract().getVerifierList().send();
                }
                if(verifiers.isStatusOk()) {
                    verifiers.data.stream().filter(Objects::nonNull).forEach(verifier->{
                        CustomNode node = new CustomNode();
                        node.updateWithNode(verifier);
                        node.setIsRecommend(CustomNode.YesNoEnum.YES.code);
                        blockChain.STAGE_BIZ_DATA.getStakingExecuteResult().stageAddNode(node);

                        CustomStaking staking = new CustomStaking();
                        staking.updateWithNode(verifier);
                        staking.setStakingIcon("");
                        staking.setIsInit(1);
                        staking.setIsSetting(1);
                        // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
                        if(blockChain.getCurValidator().get(node.getNodeId())!=null) staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
                        // 暂存至新增质押待入库列表
                        blockChain.STAGE_BIZ_DATA.getStakingExecuteResult().stageAddStaking(staking,null);
                        // 查询内置结算周期验证人初始化blockChain的curVerifier属性
                        blockChain.getCurVerifier().put(HexTool.prefix(verifier.getNodeId()),verifier);
                    });
                    BlockChainResult bcr = blockChain.exportResult();
                    service.insertOrUpdateChainInfo(Collections.EMPTY_LIST,bcr);
                    blockChain.commitResult();
                }
                // 通知质押引擎重新初始化节点缓存
                blockChain.getStakingExecute().loadNodes();
            } catch (Exception e) {
                throw new CandidateException("查询内置初始验证人列表失败："+e.getMessage());
            }
        }

        blockChain.init();
    }

    public void start () throws BlockCollectingException, SettleEpochChangeException, ConsensusEpochChangeException, ElectionEpochChangeException, CandidateException, NoSuchBeanException, IssueEpochChangeException {
        while (true) {
            // 从(已采最高区块号+1)开始构造连续的指定数量的待采区块号列表
            List <BigInteger> blockNumbers = new ArrayList <>();
            for (long blockNumber=commitBlockNumber+1; blockNumber<=(commitBlockNumber+collectBatchSize);blockNumber++) blockNumbers.add(BigInteger.valueOf(blockNumber));
            // 并行采集区块 ||||||||||||||||||||||||||||
            Result collectedResult = new Result();
            getBlockAndTransaction(blockNumbers, collectedResult);
            List <CustomBlock> blocks = collectedResult.getSortedBlocks();
            // 采集不到区块则暂停1秒, 结束本次循环
            if(blocks.size()==0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new BlockCollectingException("区块采集暂停被中断:"+e.getMessage());
                }
                continue;
            }
            // 对区块和交易做并行分析 ||||||||||||||||||||||||||||
            analyzeBlockAndTransaction(blocks);
            // 调用BlockChain实例，分析质押、提案相关业务数据
            for (CustomBlock block:blocks){
                blockChain.execute(block);
            }
            try {
                // 入库失败，立即停止，防止采集后续更高的区块号，导致不连续区块号出现
                BlockChainResult bizData = blockChain.exportResult();
                batchSaveResult(blocks, bizData);
            } catch (BusinessException e) {
                break;
            }
            blockChain.commitResult();
            // 记录已采入库最高区块号
            commitBlockNumber = blocks.get(blocks.size() - 1).getNumber();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new BlockCollectingException("区块采集暂停被中断:"+e.getMessage());
            }
        }
    }

    /**
     * 并行采集区块及交易，并转换为数据库结构
     *
     * @param blockNumbers 批量采集的区块号
     * @return
     */
    private void getBlockAndTransaction ( List <BigInteger> blockNumbers, Result result ) throws BlockCollectingException {
        // 清空重试区块号列表
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
                            CustomBlock block = new CustomBlock();
                            block.init(initData);
                            result.concurrentBlockMap.put(blockNumber.longValue(),block);
                        }catch (Exception ex){
                            logger.debug("初始化区块信息异常, 原因: {}", ex.getMessage());
                            throw ex;
                        }
                    }
                } catch (Exception e) {
                    // 把出现异常的区块号加入重试列表
                    result.retryNumbers.add(blockNumber);
                }finally {
                    latch.countDown();
                }
            })
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new BlockCollectingException("区块采集线程被中断:"+e.getMessage());
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
    private void analyzeBlockAndTransaction ( List <CustomBlock> blocks ) {
        // 对需要复杂分析的区块或交易信息，开启并行处理
        blocks.forEach(b -> {
            List <CustomTransaction> txList = b.getTransactionList();
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
                    case CREATE_PROPOSAL_PARAMETER:// 创建参数提案
                    case CREATE_PROPOSAL_TEXT:// 创建文本提案
                    case CREATE_PROPOSAL_UPGRADE:// 创建升级提案
                    case DECLARE_VERSION:// 版本声明
                    case VOTING_PROPOSAL:// 提案投票
                        stat.proposalQty++; // 提案交易数总和
                        break;
                    case DELEGATE:// 发起委托
                    case UN_DELEGATE:// 撤销委托
                        stat.delegateQty++; // 委托交易数总和
                        break;
                    case INCREASE_STAKING:// 增加自有质押
                    case CREATE_VALIDATOR:// 创建验证人
                    case EXIT_VALIDATOR:// 退出验证人
                    case REPORT_VALIDATOR:// 举报验证人
                    case EDIT_VALIDATOR:// 编辑验证人
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
    private CustomTransaction updateTransactionInfo(CustomTransaction tx) throws IOException, BeanCreateOrUpdateException {
        try {
            // 查询交易回执
            PlatonGetTransactionReceipt result = client.getWeb3j().platonGetTransactionReceipt(tx.getHash()).send();
            Optional<TransactionReceipt> receipt = result.getTransactionReceipt();
            // 如果交易回执存在，则更新交易中与回执相关的信息
            if(receipt.isPresent()) tx.updateWithTransactionReceipt(receipt.get());
        }catch (IOException e){
            logger.error("查询交易[hash={}]的回执出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }catch (BeanCreateOrUpdateException e){
            logger.error("更新交易[hash={}]的回执相关信息出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }

        // 解析交易参数，补充交易中与交易参数相关的信息
        try {
            TxParamResolver.Result txParams = TxParamResolver.analysis(tx.getInput(),blockChain.getChainConfig(),tx.getBlockNumber().toString());
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

    public void batchSaveResult(List<CustomBlock> basicData, BlockChainResult bizData){
        try{

            // 串行批量入库
            service.insertOrUpdateChainInfo(basicData,bizData);
        }catch (Exception e){
            logger.error("入库失败！原因："+e.getMessage());
            throw new BusinessException("入库失败！原因："+e.getMessage());
        }
    }
}
