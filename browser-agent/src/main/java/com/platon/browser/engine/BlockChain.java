package com.platon.browser.engine;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NetworkStatExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.*;
import com.platon.browser.engine.cache.*;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.epoch.NewIssueEpochHandler;
import com.platon.browser.engine.handler.statistic.NetworkStatStatisticHandler;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.*;
import com.platon.browser.param.DelegateParam;
import com.platon.browser.param.EditValidatorParam;
import com.platon.browser.param.IncreaseStakingParam;
import com.platon.browser.param.UnDelegateParam;
import com.platon.browser.service.DbService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameter;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:11
 * @Description: 区块链业务数据分析处理转发中心
 */
@Component
@Data
public class BlockChain {
    private static Logger logger = LoggerFactory.getLogger(BlockChain.class);
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private StakingEngine stakingExecute;
    @Autowired
    private ProposalEngine proposalExecute;
    @Autowired
    private AddressEngine addressExecute;
    @Autowired
    private RestrictingEngine restrictingEngine;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private DbService dbService;
    @Autowired
    private PlatOnClient client;
    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private NodeCacheUpdater nodeCacheUpdater;
    @Autowired
    private StakingCacheUpdater stakingCacheUpdater;
    @Autowired
    private CacheHolder cacheHolder;

    // 当前结算周期轮数
    private BigInteger curSettingEpoch;
    // 当前共识周期轮数
    private BigInteger curConsensusEpoch;
    // 增发周期轮数
    private BigInteger addIssueEpoch;
    // 当前块
    private CustomBlock curBlock;
    // 当前共识周期期望出块数=共识周期区块数/验证人数，取16位小数
    private BigDecimal curConsensusExpectBlockCount;

    // 区块奖励，每个增发周期更新一次
    private BigDecimal blockReward;
    // 当前增发周期的每个结算周期的奖励，每个增发周期更新一次
    private BigDecimal settleReward;

    @Autowired
    private NetworkStatStatisticHandler networkStatStatisticHandler;
    @Autowired
    private NewIssueEpochHandler newIssueEpochHandler;

    @Autowired
    private TpsCalcCache tpsCalcCache;

    /***
     * 以下字段业务使用说明：
     * 在当前共识周期发生选举的时候，需要对上一共识周期的验证节点计算出块率，如果发现出块率低的节点，就要看此节点是否在curValidator中，如果在则
     * 剔除
     */
    private Map <String, org.web3j.platon.bean.Node> preVerifier = new HashMap <>();// 上轮结算周期验证人
    private Map <String, org.web3j.platon.bean.Node> curVerifier = new HashMap <>();// 当前结算周期验证人
    private Map <String, org.web3j.platon.bean.Node> preValidator = new HashMap <>();// 上轮共识周期验证人
    private Map <String, org.web3j.platon.bean.Node> curValidator = new HashMap <>();// 当前共识周期验证人

    @PostConstruct
    private void init () {
        // 数据库统计数据全量初始化
        NetworkStatExample example = new NetworkStatExample();
        example.setOrderByClause(" update_time desc");
        List <NetworkStat> networkStats = networkStatMapper.selectByExample(example);
        if (!networkStats.isEmpty()) {
            BeanUtils.copyProperties(networkStats.get(0), cacheHolder.getNetworkStatCache());
        }
    }

    /**
     * 分析区块内的业务信息【总流程编排】
     *
     * @param block
     */
    public void execute ( CustomBlock block ) throws BusinessException, BlockChainException, NoSuchBeanException, InterruptedException, CandidateException, SettleEpochChangeException {
        curBlock = block;
        // 更新交易TPS
        tpsCalcCache.update(block);
        // 推算并更新共识周期和结算周期轮数
        updateEpoch(curBlock.getNumber());
        // 更新staking表中与区块统计相关的信息
        stakingCacheUpdater.updateStakingPerBlock();
        // 设置区块奖励
        curBlock.setBlockReward(blockReward.toString());
        // 分析交易信息，调用业务引擎提取业务数据信息
        analyzeTransaction();
        // 通知各引擎周期临界点事件
        epochChangeEvent();
        // 更新node表中的节点出块数信息
        nodeCacheUpdater.updateStatBlockQty();

        // 统计数据相关累加
        EventContext context = new EventContext();
        networkStatStatisticHandler.handle(context);

        // 更新当前区块的节点名称
        String nodeName = cacheHolder.getNodeNameMap().get(curBlock.getNodeId());
        curBlock.setNodeName(nodeName==null?"Unknown":nodeName);
    }

    /**
     * 根据交易信息新增或更新相关记录：
     */
    public void analyzeTransaction () throws NoSuchBeanException, BusinessException, BlockChainException {
        for (CustomTransaction tx:curBlock.getTransactionList()){
            // 统计地址相关信息
            addressExecute.execute(tx);
            // 链上执行失败的交易不予处理
            if (CustomTransaction.TxReceiptStatusEnum.FAILURE.getCode() == tx.getTxReceiptStatus()) {
                // 失败的交易需要补充节点名称到交易参数信息中
                NodeCache nodeCache = cacheHolder.getNodeCache();
                try {
                    switch (tx.getTypeEnum()) {
                        case INCREASE_STAKING: // 增持质押
                            IncreaseStakingParam isp = tx.getTxParam(IncreaseStakingParam.class);
                            CustomNode node = nodeCache.getNode(isp.getNodeId());
                            CustomStaking latestStaking = node.getLatestStaking();
                            isp.setNodeName(latestStaking.getStakingName());
                            tx.setTxInfo(JSON.toJSONString(isp));
                            break;
                        case EXIT_VALIDATOR: // 撤销质押
                            EditValidatorParam evp = tx.getTxParam(EditValidatorParam.class);
                            node = nodeCache.getNode(evp.getNodeId());
                            latestStaking = node.getLatestStaking();
                            evp.setNodeName(latestStaking.getStakingName());
                            tx.setTxInfo(JSON.toJSONString(evp));
                            break;
                        case DELEGATE: // 发起委托
                            DelegateParam dp = tx.getTxParam(DelegateParam.class);
                            node = nodeCache.getNode(dp.getNodeId());
                            latestStaking = node.getLatestStaking();
                            dp.setNodeName(latestStaking.getStakingName());
                            tx.setTxInfo(JSON.toJSONString(dp));
                            break;
                        case UN_DELEGATE: // 撤销委托
                            UnDelegateParam udp = tx.getTxParam(UnDelegateParam.class);
                            node = nodeCache.getNode(udp.getNodeId());
                            latestStaking = node.getLatestStaking();
                            udp.setNodeName(latestStaking.getStakingName());
                            tx.setTxInfo(JSON.toJSONString(udp));
                            break;
                        default:
                            return;
                    }
                }catch (NoSuchBeanException e){
                    logger.error("交易[{}]失败,且找不到对应的质押信息，无法补充交易中的节点名称信息！",tx.getHash());
                }
            }

            // 调用交易分析引擎分析交易，以补充相关数据
            switch (tx.getTypeEnum()) {
                case CREATE_VALIDATOR: // 创建验证人
                case EDIT_VALIDATOR: // 编辑验证人
                case INCREASE_STAKING: // 增持质押
                case EXIT_VALIDATOR: // 撤销质押
                case DELEGATE: // 发起委托
                case UN_DELEGATE: // 撤销委托
                case REPORT_VALIDATOR: // 举报多签验证人
                    stakingExecute.execute(tx);
                    break;
                case CREATE_PROPOSAL_TEXT: // 创建文本提案
                case CREATE_PROPOSAL_UPGRADE: // 创建升级提案
                case CANCEL_PROPOSAL: // 创建参数提案
                case VOTING_PROPOSAL: // 给提案投票
                case DUPLICATE_SIGN: // 双签举报
                case DECLARE_VERSION://版本声明
                    proposalExecute.execute(tx);
                    break;
                case CREATE_RESTRICTING://创建锁仓计划
                    restrictingEngine.execute(tx);
                    break;
                case CONTRACT_CREATION: // 合约发布(合约创建)
                    logger.debug("合约发布(合约创建): txHash({}),contract({})", tx.getHash(), tx.getTo());
                    break;
                case TRANSFER: // 转账
                    logger.debug("转账: txHash({}),from({}),to({})", tx.getHash(), tx.getFrom(), tx.getTo());
                    break;
                case OTHERS: // 其它
                case MPC:
				default:
					break;
            }
        }
    }


    /**
     * 导出需要入库的业务数据
     *
     * @return
     */
    public BlockChainStage exportResult () {
        return cacheHolder.getStageData();
    }

    /**
     * 清除分析后的业务数据
     */
    public void commitResult () {
        cacheHolder.getStageData().clear();
    }

    /**
     * 创建地址实体并入库
     * @param address
     * @param typeEnum
     * @throws BlockChainException
     */
    public void createAddress(String address,CustomAddress.TypeEnum typeEnum) throws BlockChainException {
        if(StringUtils.isBlank(address)) {
            throw new BlockChainException("参数地址不能为空!");
        }
        address=address.toLowerCase().startsWith("0x")?address:"0x"+address;
        try {
            cacheHolder.getAddressCache().getAddress(address);
        }catch (NoSuchBeanException e){
            CustomAddress customAddress = new CustomAddress();
            customAddress.setAddress(address);
            customAddress.setType(typeEnum.getCode());
            cacheHolder.getAddressCache().add(customAddress);
            cacheHolder.getStageData().getAddressStage().insertAddress(customAddress);
        }
    }
    /**
     * 周期变更通知：
     * 通知各钩子方法处理周期临界点事件，以便更新与周期切换相关的信息
     */
    public void epochChangeEvent() throws CandidateException, NoSuchBeanException, SettleEpochChangeException, InterruptedException {
        // 根据区块号是否整除周期来触发周期相关处理方法
        Long blockNumber = curBlock.getNumber();

        // (当前块号+选举回退块数)%共识周期区块数==0 && 当前共识周期不是第一个共识周期
        if ((blockNumber+chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0&&curConsensusEpoch.longValue()>1) {
            logger.debug("选举验证人：Block Number({})", blockNumber);
            stakingExecute.onElectionDistance();

        }

        if (blockNumber % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            logger.debug("共识周期切换：Block Number({})", blockNumber);
            stakingExecute.onNewConsEpoch();

        }

        if (blockNumber % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            logger.debug("结算周期切换：Block Number({})", blockNumber);
            stakingExecute.onNewSettingEpoch();

        }

        if (blockNumber % chainConfig.getAddIssuePeriodBlockCount().longValue() == 0) {
            logger.debug("增发周期切换：Block Number({})", blockNumber);
            EventContext context = new EventContext();
            newIssueEpochHandler.handle(context);
        }
    }


    /**
     * 更新共识周期和结算周期轮数
     * 根据区块号推算并更新共识周期和结算周期轮数
     * @param blockNumber
     */
    public void updateEpoch(Long blockNumber){
        this.curConsensusEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getConsensusPeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        // 计算结算周期轮数
        this.curSettingEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getSettlePeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        //计算增发周期轮数
        this.addIssueEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getAddIssuePeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
    }

    /**
     * 更新BlockChain实例中的结算周期质押奖励和区块奖励属性
     * @param blockNumber
     * @throws IssueEpochChangeException
     */
    public void updateReward(Long blockNumber) {
        // 激励池账户地址
        String incentivePoolAccountAddr = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress();
        BigInteger incentivePoolAccountBalance;
        while (true){
            try {
                // 根据激励池地址查询前一增发周期末激励池账户余额：查询前一增发周期末块高时的激励池账户余额
                incentivePoolAccountBalance = client.getWeb3j()
                        .platonGetBalance(incentivePoolAccountAddr, DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)))
                        .send().getBalance();
                break;
            } catch (Exception e) {
                logger.error("查询激励池(addr={})在块号({})的账户余额失败,将重试:{}",incentivePoolAccountAddr,blockNumber,e.getMessage());
                client.updateCurrentValidWeb3j();
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (Exception ex) {
                    logger.error("",ex);
                }
            }
        }

        logger.debug("区块号位于({})时激励池账户余额:{}",blockNumber,incentivePoolAccountBalance);
        // 激励池中的质押奖励部分
        BigDecimal stakingPart = new BigDecimal(incentivePoolAccountBalance)
                .multiply(chainConfig.getStakeRewardRate()); // 取出激励池余额中属于质押奖励的部分
        logger.debug("质押奖励部分:{}",stakingPart);
        // 每个增发周期的总结算周期数
        BigDecimal settleEpochCountPerIssue = new BigDecimal(chainConfig.getSettlePeriodCountPerIssue());
        // 每个结算周期的质押奖励
        this.settleReward = stakingPart.divide(settleEpochCountPerIssue,0, RoundingMode.FLOOR); // 除以结算周期轮数，向下取整
        logger.debug("当前结算周期奖励:{}",settleReward);
        // 激励池中的出块奖励部分
        BigDecimal blockingPart = new BigDecimal(incentivePoolAccountBalance).multiply(chainConfig.getBlockRewardRate()); // 取出激励池余额中属于区块奖励的部分
        logger.debug("区块奖励部分:{}",stakingPart);
        // 每个增发周期的总块数
        BigDecimal issuePeriodBlockCount = new BigDecimal(chainConfig.getAddIssuePeriodBlockCount());
        // 出块奖励
        this.blockReward = blockingPart.divide(issuePeriodBlockCount,0, RoundingMode.FLOOR); // 除以一个增发周期的总区块数，向下取整
        logger.debug("当前区块奖励:{}",blockReward);
    }

    /**
     * 更新当前共识周期期望出块数
     */
    public void updateCurConsensusExpectBlockCount(Integer validatorCount){
        // 当前共识周期期望出块数=共识周期区块数/验证人数，取16位小数
        BigDecimal ccebc = new BigDecimal(chainConfig.getConsensusPeriodBlockCount())
                .divide(new BigDecimal(validatorCount),16,RoundingMode.FLOOR);
        curConsensusExpectBlockCount=ccebc;
    }
}
