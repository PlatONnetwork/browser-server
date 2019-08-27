package com.platon.browser.task;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.enums.ReceiveTypeEnum;
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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

import static com.platon.browser.engine.BlockChain.NODE_NAME_MAP;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 09:14
 * @Description: 区块和交易同步任务
 */
@Component
public class BlockSyncTask {
    private static Logger logger = LoggerFactory.getLogger(BlockSyncTask.class);
    private static ExecutorService THREAD_POOL;

    @Autowired
    private CustomBlockMapper customBlockMapper;
    @Autowired
    private DbService dbService;
    @Autowired
    private BlockChain blockChain;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatonClient client;

    // 已采集入库的最高块
    private long commitBlockNumber = 0;

    // 每一批次采集区块的数量
    @Value("${platon.web3j.collect.batch-size}")
    private int collectBatchSize;

    @Data
    static class Result {
        // 并发采集的块信息，无序
        public Map <Long, CustomBlock> concurrentBlockMap = new ConcurrentHashMap <>();
        // 由于异常而未采集的区块号列表
        private Set <BigInteger> retryNumbers = new CopyOnWriteArraySet <>();
        // 已排序的区块信息列表
        private List <CustomBlock> sortedBlocks = new LinkedList <>();

        private List <CustomBlock> getSortedBlocks () {
            if (sortedBlocks.size() == 0) {
                sortedBlocks.addAll(concurrentBlockMap.values());
                sortedBlocks.sort(Comparator.comparing(Block::getNumber));
            }
            return sortedBlocks;
        }
    }

    /**
     * 初始化已有业务数据
     */
    public void init () throws CandidateException, IssueEpochChangeException {
        THREAD_POOL = Executors.newFixedThreadPool(collectBatchSize);
        // 从数据库查询最高块号，赋值给commitBlockNumber
        Long maxBlockNumber = customBlockMapper.selectMaxBlockNumber();
        // 更新当前所在周期的区块奖励和结算周期质押奖励
        if (maxBlockNumber != null && maxBlockNumber > 0) {
            commitBlockNumber = maxBlockNumber;
            blockChain.updateReward(maxBlockNumber);
        }else{
            blockChain.updateReward(0l);
        }

        /*
         * 从第一块同步的时候，结算周期验证人和共识周期验证人是链上内置的
         * 查询内置共识周期验证人初始化blockChain的curValidator属性
         * 查询内置结算周期验证人初始化blockChain的curVerifier属性
          */
        if(maxBlockNumber==null){
            // 如果库里区块为空，则：
            try {
                // 根据区块号0查询共识周期验证人，以便对结算周期验证人设置共识标识
                BaseResponse<List<Node>> result = client.getHistoryValidatorList(BigInteger.ZERO);
                if(!result.isStatusOk()){
                    logger.debug("查询实时共识周期验证人列表...");
                    result = client.getNodeContract().getValidatorList().send();
                    if(!result.isStatusOk()){
                        throw new CandidateException("底层链查询实时共识周期验证节点列表出错:"+result.errMsg);
                    }
                }
                // 查询内置共识周期验证人初始化blockChain的curValidator属性
                Set<String> validatorSet = new HashSet<>();
                result.data.forEach(node->validatorSet.add(HexTool.prefix(node.getNodeId())));

                // 根据区块号0查询结算周期验证人列表并入库
                result = client.getHistoryVerifierList(BigInteger.ZERO);
                if(!result.isStatusOk()){
                    logger.debug("查询实时结算周期验证人列表...");
                    result = client.getNodeContract().getVerifierList().send();
                    if(!result.isStatusOk()){
                        throw new CandidateException("底层链查询实时结算周期验证节点列表出错:"+result.errMsg);
                    }
                }
                Set<String> verifierSet = new HashSet<>();
                result.data.forEach(node->verifierSet.add(HexTool.prefix(node.getNodeId())));

                result = client.getNodeContract().getCandidateList().send();
                if(!result.isStatusOk()){
                    throw new CandidateException("底层链查询候选验证节点列表出错:"+result.errMsg);
                }

                result.data.stream().filter(Objects::nonNull).forEach(candidate->{
                    CustomNode node = new CustomNode();
                    node.updateWithNode(candidate);
                    node.setIsRecommend(CustomNode.YesNoEnum.YES.code);
                    node.setStatVerifierTime(BigInteger.ONE.intValue());
                    node.setStatExpectBlockQty(chainConfig.getExpectBlockCount().longValue());
                    BlockChain.STAGE_DATA.getStakingStage().insertNode(node);

                    CustomStaking staking = new CustomStaking();
                    staking.updateWithNode(candidate);
                    staking.setIsInit(1);
                    staking.setIsSetting(1);
                    BigDecimal stakingLocked = Convert.toVon(blockChain.getChainConfig().getInitValidatorStakingLockedAmount(), Convert.Unit.LAT);
                    staking.setStakingLocked(stakingLocked.toString());
                    // 如果当前候选节点在共识周期验证人列表，则标识其为共识周期节点
                    if(validatorSet.contains(node.getNodeId())) staking.setIsConsensus(CustomStaking.YesNoEnum.YES.code);
                    if(verifierSet.contains(node.getNodeId())) staking.setIsSetting(CustomStaking.YesNoEnum.YES.code);
                    // 暂存至新增质押待入库列表
                    BlockChain.STAGE_DATA.getStakingStage().insertStaking(staking);

                    // 更新节点名称映射缓存
                    NODE_NAME_MAP.put(staking.getNodeId(),staking.getStakingName());
                });
                BlockChainStage bcr = blockChain.exportResult();
                batchSave(Collections.emptyList(),bcr);
                blockChain.commitResult();

                // 通知质押引擎重新初始化节点缓存
                blockChain.getStakingExecute().loadNodes();
            } catch (Exception e) {
                throw new CandidateException("查询内置初始验证人列表失败："+e.getMessage());
            }
        }
    }

    public void start() throws BlockCollectingException, SettleEpochChangeException, ConsensusEpochChangeException, ElectionEpochChangeException, CandidateException, NoSuchBeanException, IssueEpochChangeException, BusinessException, BlockChainException {
        while (true) {
            // 从(已采最高区块号+1)开始构造连续的指定数量的待采区块号列表
            List <BigInteger> blockNumbers = new ArrayList <>();
            // 当前链上最新区块号
            BigInteger curChainBlockNumber;
            try {
                curChainBlockNumber = client.getWeb3j().platonBlockNumber().send().getBlockNumber();
            } catch (IOException e) {
                throw new BlockCollectingException("取链上最新区块号失败:"+e.getMessage());
            }
            for (long blockNumber=commitBlockNumber+1; blockNumber<=(commitBlockNumber+collectBatchSize);blockNumber++) {
                // 如果块号>当前链上块号,则不再累加
                if(blockNumber>curChainBlockNumber.longValue()) break;
                blockNumbers.add(BigInteger.valueOf(blockNumber));
            }
            if(blockNumbers.size()==0){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new BlockCollectingException("区块采集暂停被中断:"+e.getMessage());
                }
                continue;
            }
            // 并行采块 ξξξξξξξξξξξξξξξξξξξξξξξξξξξ
            Result collectedResult = new Result();
            parallelCollect(blockNumbers, collectedResult);
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
            // 并行分析 ξξξξξξξξξξξξξξξξξξξξξξξξξξξ
            parallelAnalyze(blocks);
            // 调用BlockChain实例，分析质押、提案相关业务数据
            for (CustomBlock block:blocks){
                blockChain.execute(block);
            }
            try {
                // 入库失败，立即停止，防止采集后续更高的区块号，导致不连续区块号出现
                BlockChainStage bizData = blockChain.exportResult();
                batchSave(blocks, bizData);
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
     * @return void
     */
    private void parallelCollect ( List <BigInteger> blockNumbers, Result result ) throws BlockCollectingException {
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
                            block.updateWithBlock(initData);
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
            Set <Long> collected = result.concurrentBlockMap.keySet();
            long start = Collections.min(collected), end = Collections.max(collected);
            for (long i = start; i < end; i++) {
                if (!collected.contains(i)) result.retryNumbers.add(BigInteger.valueOf(i));
            }
        }

        if (result.retryNumbers.size() > 0) {
            logger.debug("区块重试列表：{}", result.retryNumbers);
            parallelCollect(new ArrayList <>(result.retryNumbers), result);
        }
    }

    /**
     * 并行分析区块及交易
     */
    private void parallelAnalyze( List <CustomBlock> blocks ) {
        // 对需要复杂分析的区块或交易信息，开启并行处理
        blocks.forEach(b -> {
            List <CustomTransaction> txList = b.getTransactionList();
            CountDownLatch latch = new CountDownLatch(txList.size());
            txList.forEach(tx ->THREAD_POOL.submit(() -> {
                try {
                    updateTransaction(tx);
                } catch (Exception e) {
                    logger.error("更新交易信息错误：{}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            }));
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 汇总区块相关的统计信息
        class Stat {
            private int transferQty=0,stakingQty=0,proposalQty=0,delegateQty=0,txGasLimit=0;
            private BigDecimal txFee = BigDecimal.ZERO;
        }
        blocks.forEach(block->{
            Stat stat = new Stat();
            block.getTransactionList().forEach(transaction->{
                switch (transaction.getTypeEnum()){
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
                stat.txFee = stat.txFee.add(new BigDecimal(transaction.getActualTxCost()));
                // 累加当前区块内所有交易的GasLimit
                stat.txGasLimit = stat.txGasLimit+Integer.parseInt(transaction.getGasLimit());
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
    private void updateTransaction(CustomTransaction tx) throws IOException, BeanCreateOrUpdateException {
        try {
            // 查询交易回执
            PlatonGetTransactionReceipt result = client.getWeb3j().platonGetTransactionReceipt(tx.getHash()).send();
            Optional<TransactionReceipt> receipt = result.getTransactionReceipt();
            // 如果交易回执存在，则更新交易中与回执相关的信息
            if(receipt.isPresent()) tx.updateWithTransactionReceipt(receipt.get(),BlockChainConfig.INNER_CONTRACT_ADDR);
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
            tx.setTxInfo(JSON.toJSONString(txParams.getParam()));
            tx.setTxType(String.valueOf(txParams.getTxTypeEnum().code));
            tx.setReceiveType(ReceiveTypeEnum.CONTRACT.name().toLowerCase());
            if(null != tx.getValue() && ! InnerContractAddrEnum.addresses.contains(tx.getTo())){
                tx.setTxType(String.valueOf(CustomTransaction.TxTypeEnum.TRANSFER.code));
                tx.setReceiveType(ReceiveTypeEnum.ACCOUNT.name().toLowerCase());
            }
        }catch (Exception e){
            logger.error("交易[hash={}]的参数解析出错:{}",tx.getHash(),e.getMessage());
            throw e;
        }
    }

    private void batchSave(List<CustomBlock> basicData, BlockChainStage bizData) throws BusinessException {
        try{
            // 串行批量入库
            dbService.insertOrUpdate(basicData,bizData);
            // 缓存整理
            BlockChain.NODE_CACHE.sweep();
            BlockChain.PROPOSALS_CACHE.sweep();
        }catch (Exception e){
            throw new BusinessException("数据批量入库出错："+e.getMessage());
        }
    }
}
