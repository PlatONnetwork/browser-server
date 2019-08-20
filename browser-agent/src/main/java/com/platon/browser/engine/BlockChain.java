package com.platon.browser.engine;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NetworkStatExample;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.config.BlockChainConfig;
import com.platon.browser.engine.result.BlockChainResult;
import com.platon.browser.exception.*;
import com.platon.browser.service.DbService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:11
 * @Description:
 */
@Component
@Data
public class BlockChain {
    private static Logger logger = LoggerFactory.getLogger(BlockChain.class);
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private StakingExecute stakingExecute;
    @Autowired
    private ProposalExecute proposalExecute;
    @Autowired
    private AddressExecute addressExecute;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private StakingMapper stakingMapper;
    @Autowired
    private DbService dbService;
    @Autowired
    private PlatonClient client;
    @Autowired
    private NetworkStatMapper networkStatMapper;

    @Autowired
    private BlockChainHandler blockChainHandler;

    // 业务数据暂存容器
    public static final BlockChainResult STAGE_BIZ_DATA = new BlockChainResult();
    // 全量数据(质押相关)，需要根据业务变化，保持与数据库一致
    public static final NodeCache NODE_CACHE = new NodeCache();
    // 全量数据(提案相关)，需要根据业务变化，保持与数据库一致
    public static final Map <String, CustomProposal> PROPOSALS_CACHE = new HashMap <>();
    // 全量统计数据
    public static final CustomNetworkStat NETWORK_STAT_CACHE = new CustomNetworkStat();
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


    /***
     * 以下字段业务使用说明：
     * 在当前共识周期发生选举的时候，需要对上一共识周期的验证节点计算出块率，如果发现出块率低的节点，就要看此节点是否在curValidator中，如果在则
     * 剔除
     */
    // 上轮结算周期验证人
    private Map <String, org.web3j.platon.bean.Node> preVerifier = new HashMap <>();
    // 当前结算周期验证人
    private Map <String, org.web3j.platon.bean.Node> curVerifier = new HashMap <>();
    // 上轮共识周期验证人
    private Map <String, org.web3j.platon.bean.Node> preValidator = new HashMap <>();
    // 当前共识周期验证人
    private Map <String, org.web3j.platon.bean.Node> curValidator = new HashMap <>();

    @PostConstruct
    private void init () {
        // 初始化区块处理器
        blockChainHandler.init(this);

        // 计算每个增发周期内有几个结算周期：每个增发周期总块数/每个结算周期总块数
        settleEpochCountPerIssueEpoch = chainConfig.getAddIssuePeriodBlockCount().divide(chainConfig.getSettlePeriodBlockCount());

        // 数据库统计数据全量初始化
        NetworkStatExample example = new NetworkStatExample();
        example.setOrderByClause(" update_time desc");
        List <NetworkStat> networkStats = networkStatMapper.selectByExample(example);
        if (networkStats.size() != 0) {
            BeanUtils.copyProperties(networkStats.get(0), NETWORK_STAT_CACHE);
        }else {
            BeanUtils.copyProperties(new CustomNetworkStat(),NETWORK_STAT_CACHE);
        }
    }

    /**
     * 分析区块内的业务信息
     *
     * @param block
     */
    public void execute ( CustomBlock block ) throws SettleEpochChangeException, CandidateException, ConsensusEpochChangeException, ElectionEpochChangeException, NoSuchBeanException {
        curBlock = block;
        // 推算并更新共识周期和结算周期
        blockChainHandler.updateEpoch();
        // 更新共识周期验证人
        blockChainHandler.updateValidator();
        // 更新结算周期验证人列表
        blockChainHandler.updateVerifier();
        // 分析交易信息, 通知质押引擎补充质押相关信息，通知提案引擎补充提案相关信息
        blockChainHandler.analyzeTransaction();
        // 通知各引擎周期临界点事件
        blockChainHandler.periodChangeNotify();
        //统计数据相关累加
        blockChainHandler.updateWithNetworkStat();
        // 更新node表中的节点出块数信息
        blockChainHandler.updateNodeStatBlockQty();
        // 更新staking表中与区块统计相关的信息
        blockChainHandler.updateStakingRelative();
    }

    /**
     * 导出需要入库的业务数据
     *
     * @return
     */
    public BlockChainResult exportResult () {
        return STAGE_BIZ_DATA;
    }

    /**
     * 清除分析后的业务数据
     */
    public void commitResult () {
        STAGE_BIZ_DATA.clear();
    }

}
