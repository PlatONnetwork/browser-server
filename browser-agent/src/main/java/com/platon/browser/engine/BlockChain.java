package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NetworkStatExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.cache.*;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.epoch.NewIssueEpochHandler;
import com.platon.browser.engine.handler.statistic.NetworkStatStatisticHandler;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BlockChainException;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.IssueEpochChangeException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.service.DbService;
import lombok.Data;
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
    private PlatonClient client;
    @Autowired
    private NetworkStatMapper networkStatMapper;
    @Autowired
    private NodeCacheUpdater nodeCacheUpdater;
    @Autowired
    private StakingCacheUpdater stakingCacheUpdater;

    // 节点名称缓存 <节点ID-节点名称>
    public static final Map<String,String> NODE_NAME_MAP = new HashMap<>();
    // 业务数据暂存容器
    public static final BlockChainStage STAGE_DATA = new BlockChainStage();
    // 全量数据(质押相关)，需要根据业务变化，保持与数据库一致
    public static final NodeCache NODE_CACHE = new NodeCache();
    // 全量数据(提案相关)，需要根据业务变化，保持与数据库一致
    public static final ProposalCache PROPOSALS_CACHE = new ProposalCache();
    // 全量统计数据
    public static final CustomNetworkStat NETWORK_STAT_CACHE = new CustomNetworkStat();
    // 全量数据，需要根据业务变化，保持与数据库一致
    public static final AddressCache ADDRESS_CACHE = new AddressCache();
    // 当前结算周期轮数
    private BigInteger curSettingEpoch;
    // 当前共识周期轮数
    private BigInteger curConsensusEpoch;
    // 增发周期轮数
    private BigInteger addIssueEpoch;
    // 当前块
    private CustomBlock curBlock;

    // 区块奖励，每个增发周期更新一次
    private BigDecimal blockReward;
    // 当前增发周期的每个结算周期的奖励，每个增发周期更新一次
    private BigDecimal settleReward;

    // 每个增发周期内有几个结算周期
    private BigInteger settleEpochCountPerIssueEpoch;


    @Autowired
    private NetworkStatStatisticHandler networkStatStatisticHandler;
    @Autowired
    private NewIssueEpochHandler newIssueEpochHandler;


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
    private void init () throws Exception {
        // 计算每个增发周期内有几个结算周期：每个增发周期总块数/每个结算周期总块数
        settleEpochCountPerIssueEpoch = chainConfig.getAddIssuePeriodBlockCount().divide(chainConfig.getSettlePeriodBlockCount());
        // 数据库统计数据全量初始化
        NetworkStatExample example = new NetworkStatExample();
        example.setOrderByClause(" update_time desc");
        List <NetworkStat> networkStats = networkStatMapper.selectByExample(example);
        if (networkStats.size() != 0) {
            BeanUtils.copyProperties(networkStats.get(0), NETWORK_STAT_CACHE);
        }
    }

    /**
     * 分析区块内的业务信息【总流程编排】
     *
     * @param block
     */
    public void execute ( CustomBlock block ) throws Exception {
        curBlock = block;
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
        String nodeName = NODE_NAME_MAP.get(curBlock.getNodeId());
        curBlock.setNodeName(nodeName==null?"Unknown":nodeName);
    }

    /**
     * 根据交易信息新增或更新相关记录：
     */
    public void analyzeTransaction () throws NoSuchBeanException, BusinessException, BlockChainException {
        for (CustomTransaction tx:curBlock.getTransactionList()){
            // 统计地址相关信息
            addressExecute.execute(tx,this);
            // 链上执行失败的交易不予处理
            if (CustomTransaction.TxReceiptStatusEnum.FAILURE.code == tx.getTxReceiptStatus()) return;
            // 调用交易分析引擎分析交易，以补充相关数据
            switch (tx.getTypeEnum()) {
                case CREATE_VALIDATOR: // 创建验证人
                case EDIT_VALIDATOR: // 编辑验证人
                case INCREASE_STAKING: // 增持质押
                case EXIT_VALIDATOR: // 撤销质押
                case DELEGATE: // 发起委托
                case UN_DELEGATE: // 撤销委托
                case REPORT_VALIDATOR: // 举报多签验证人
                    stakingExecute.execute(tx, this);
                    break;
                case CREATE_PROPOSAL_TEXT: // 创建文本提案
                case CREATE_PROPOSAL_UPGRADE: // 创建升级提案
                case CANCEL_PROPOSAL: // 创建参数提案
                case VOTING_PROPOSAL: // 给提案投票
                case DUPLICATE_SIGN: // 双签举报
                    proposalExecute.execute(tx, this);
                    break;
                case CREATE_RESTRICTING://创建锁仓计划
                    restrictingEngine.execute(tx,this);
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
        return STAGE_DATA;
    }

    /**
     * 清除分析后的业务数据
     */
    public void commitResult () {
        STAGE_DATA.clear();
    }


    /**
     * 周期变更通知：
     * 通知各钩子方法处理周期临界点事件，以便更新与周期切换相关的信息
     */
    public void epochChangeEvent() throws Exception {
        // 根据区块号是否整除周期来触发周期相关处理方法
        Long blockNumber = curBlock.getNumber();

        // (当前块号+选举回退块数)%共识周期区块数==0 && 当前共识周期不是第一个共识周期
        if ((blockNumber+chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0&&curConsensusEpoch.longValue()>1) {
            logger.debug("选举验证人：Block Number({})", blockNumber);
            stakingExecute.onElectionDistance(curBlock, this);

        }

        if (blockNumber % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            logger.debug("共识周期切换：Block Number({})", blockNumber);
            stakingExecute.onNewConsEpoch(curBlock, this);

        }

        if (blockNumber % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            logger.debug("结算周期切换：Block Number({})", blockNumber);
            stakingExecute.onNewSettingEpoch(curBlock, this);

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
        BigInteger curConsensusEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getConsensusPeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        this.curConsensusEpoch=curConsensusEpoch;
        // 计算结算周期轮数
        BigInteger curSettingEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getSettlePeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        this.curSettingEpoch=curSettingEpoch;
        //计算增发周期轮数
        BigInteger addIssueEpoch = BigInteger.valueOf(BigDecimal.valueOf(blockNumber)
                .divide(BigDecimal.valueOf(chainConfig.getAddIssuePeriodBlockCount().longValue()), 0, RoundingMode.CEILING).longValue());
        this.addIssueEpoch = addIssueEpoch;
    }

    /**
     * 更新BlockChain实例中的结算周期质押奖励和区块奖励属性
     * @param blockNumber
     * @throws IssueEpochChangeException
     */
    public void updateReward(Long blockNumber) throws IssueEpochChangeException {
        // 激励池账户地址
        String incentivePoolAccountAddr = InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.address;
        try {
            // 根据激励池地址查询前一增发周期末激励池账户余额：查询前一增发周期末块高时的激励池账户余额
            BigInteger incentivePoolAccountBalance = client.getWeb3j()
                    .platonGetBalance(incentivePoolAccountAddr, DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)))
                    .send().getBalance();
            logger.debug("区块号位于({})时激励池账户余额:{}",blockNumber,incentivePoolAccountBalance.toString());
            // 激励池中的质押奖励部分
            BigDecimal stakingPart = new BigDecimal(incentivePoolAccountBalance.toString())
                    .multiply(chainConfig.getStakeRewardRate()); // 取出激励池余额中属于质押奖励的部分
            logger.debug("质押奖励部分:{}",stakingPart.toString());
            // 每个增发周期的总结算周期数
            BigDecimal settleEpochCountPerIssue = new BigDecimal(settleEpochCountPerIssueEpoch);
            // 每个结算周期的质押奖励
            BigDecimal settleReward = stakingPart
                    .divide(settleEpochCountPerIssue,0, RoundingMode.FLOOR); // 除以结算周期轮数，向下取整
            this.settleReward=settleReward;
            logger.debug("当前结算周期奖励:{}",settleReward.toString());
            // 激励池中的出块奖励部分
            BigDecimal blockingPart = new BigDecimal(incentivePoolAccountBalance)
                    .multiply(chainConfig.getBlockRewardRate()); // 取出激励池余额中属于区块奖励的部分
            logger.debug("区块奖励部分:{}",stakingPart.toString());
            // 每个增发周期的总块数
            BigDecimal issuePeriodBlockCount = new BigDecimal(chainConfig.getAddIssuePeriodBlockCount());
            // 出块奖励
            BigDecimal blockReward = blockingPart
                    .divide(issuePeriodBlockCount,0, RoundingMode.FLOOR); // 除以一个增发周期的总区块数，向下取整
            this.blockReward=blockReward;
            logger.debug("当前区块奖励:{}",blockReward.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IssueEpochChangeException("查询激励池(addr="+incentivePoolAccountAddr+")在块号("+blockNumber+")的账户余额失败:"+e.getMessage());
        }
    }
}
