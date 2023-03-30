package com.platon.browser.config;

import com.platon.browser.bean.CustomStaking;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.ConfigLoadingException;
import com.platon.protocol.core.methods.response.bean.EconomicConfig;
import com.platon.utils.Convert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

/**
 * 链参数统一配置项
 *
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */

@Slf4j
@DependsOn("networkParams")
@Data
@Configuration
@ConfigurationProperties(prefix = "platon")
public class BlockChainConfig {

    private static Set<String> INNER_CONTRACT_ADDR;

    @Resource
    private ConfigMapper configMapper;

    @Resource
    private PlatOnClient client;

    static {
        File saltFile = FileUtils.getFile(System.getProperty("user.dir"), "jasypt.properties");
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(saltFile)) {
            properties.load(in);
            String salt = properties.getProperty("jasypt.encryptor.password");
            if (StringUtils.isBlank(salt)) {
                throw new ConfigLoadingException("加密盐不能为空!");
            }
            salt = salt.trim();
            System.setProperty("JASYPT_ENCRYPTOR_PASSWORD", salt);
            log.debug("salt:{}", salt);
        } catch (IOException | ConfigLoadingException e) {
            log.error("加载解密文件出错", e);
            System.exit(1);
        }
    }

    public Set<String> getInnerContractAddr() {
        return Collections.unmodifiableSet(INNER_CONTRACT_ADDR);
    }

    /*******************以下参数通过rpc接口debug_economicConfig获取*******************/
    @Value("${platon.chainId}")
    private long chainId;

    //【通用】默认每个区块的最大Gas
    @Value("${platon.maxBlockGasLimit}")
    private BigDecimal maxBlockGasLimit;

    /**
     * 【通用】每个验证人每个共识周期出块数量目标值
     */
    private BigInteger expectBlockCount;

    /**
     * 【通用】每个共识轮验证节点数量
     */
    private BigInteger consensusValidatorCount;

    /**
     * 【通用】每个结算周期验证节点数量
     */
    private BigInteger settlementValidatorCount;

    /**
     * 【通用】每个增发周期的分钟数
     */
    private BigInteger additionalCycleMinutes;

    /**
     * 【通用】每个结算周期的分钟数
     */
    private BigInteger settlementCycleMinutes;

    /**
     * 【通用】每个增发周期内的结算周期数
     */
    private BigInteger settlePeriodCountPerIssue;

    /**
     * 【通用】出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
     */
    private BigInteger blockInterval;

    /**
     * 【通用】共识轮区块数 = expectBlockCount x consensusValidatorCount
     */
    private BigInteger consensusPeriodBlockCount;

    /**
     * 【通用】每个结算周期区块总数=ROUND_DOWN(结算周期规定的分钟数x60/(出块间隔x共识轮区块数))x共识轮区块数
     */
    private BigInteger settlePeriodBlockCount;

    /**
     * 【通用】每个增发周期区块总数=ROUND_DOWN(增发周期的时间x60/(出块间隔x结算周期区块数))x结算周期区块数
     */
    private BigInteger addIssuePeriodBlockCount;

    /**
     * 【通用】PlatOn基金会账户地址
     */
    private String platOnFundAccount;

    /**
     * 【通用】PlatOn基金会账户初始余额
     */
    private BigDecimal platOnFundInitAmount;

    /**
     * 【通用】开发者激励基金账户地址
     */
    private String communityFundAccount;

    /**
     * 【通用】开发者激励基金账户初始余额
     */
    private BigDecimal communityFundInitAmount;

    /**
     * 【质押】质押门槛: 创建验证人最低的质押Token数(VON)
     */
    private BigDecimal stakeThreshold;

    /**
     * 【质押】委托门槛(VON)
     */
    private BigDecimal delegateThreshold;

    /**
     * 【质押】节点质押退回锁定的结算周期数
     */
    private BigInteger unStakeRefundSettlePeriodCount;

    /**
     * 【质押】解委托锁定周期数
     */
    private BigInteger unDelegateFreezeDurationCount;

    /**
     * 【惩罚】双签奖励百分比
     */
    private BigDecimal duplicateSignRewardRate;

    /**
     * 【惩罚】双签处罚百分比
     */
    private BigDecimal duplicateSignSlashRate;

    /**
     * 【惩罚】举报证据生效周期数
     */
    private BigDecimal evidenceValidEpoch;

    /**
     * 【惩罚】扣除区块奖励的个数
     */
    private BigDecimal slashBlockRewardCount;

    /**
     * 【治理】文本提案参与率: >
     */
    private BigDecimal minProposalTextParticipationRate;

    /**
     * 【治理】文本提案支持率：>=
     */
    private BigDecimal minProposalTextSupportRate;

    /**
     * 【治理】取消提案参与率: >
     */
    private BigDecimal minProposalCancelParticipationRate;

    /**
     * 【治理】取消提案支持率：>=
     */
    private BigDecimal minProposalCancelSupportRate;

    /**
     * 【治理】升级提案通过率
     */
    private BigDecimal minProposalUpgradePassRate;

    /**
     * 【治理】文本提案默认结束轮数
     */
    private BigDecimal proposalTextConsensusRounds;

    /**
     * 【治理】设置预升级开始轮数
     */
    private BigDecimal versionProposalActiveConsensusRounds;

    /**
     * 【治理】参数提案的投票持续最长的时间（单位：s）
     */
    private BigInteger paramProposalVoteDurationSeconds;

    /**
     * 【治理】参数提案投票参与率阈值（参数提案投票通过条件之一：大于此值，则参数提案投票通过)
     */
    private BigDecimal paramProposalVoteRate;

    /**
     * 【治理】参数提案投票支持率阈值（参数提案投票通过条件之一：大于等于此值，则参数提案投票通过
     */
    private BigDecimal paramProposalSupportRate;

    /**
     * 【奖励】激励池分配给出块激励的比例
     */
    private BigDecimal blockRewardRate;

    /**
     * 【奖励】激励池分配给质押激励的比例
     */
    private BigDecimal stakeRewardRate;

    /**
     * 【奖励】Platon基金会年限
     */
    private BigInteger platOnFoundationYear;

    /**
     * 【通用】当前增发周期轮数
     */
    private BigDecimal issueEpochRound;

    /**
     * 【通用】当前增发周期开始区块号
     */
    private BigDecimal issueEpochStartBlockNumber;

    /**
     * 【通用】当前增发周期结束区块号
     */
    private BigDecimal issueEpochEndBlockNumber;

    /**
     * 【锁仓】最小释放金额(LAT)
     */
    private BigDecimal restrictingMinimumRelease;

    /*******************以下参数通过从应用配置文件获取*******************/

    /**
     * 质押节点统计年化率最多取多少个连续周期
     */
    private BigInteger maxSettlePeriodCount4AnnualizedRateStat;

    /**
     * PlatON初始总发行量(ATP)
     */
    private BigDecimal initIssueAmount;

    /**
     * 每年固定增发比例
     */
    private BigDecimal addIssueRate;

    /**
     * 每年增发分配给激励池的比例
     */
    private BigDecimal incentiveRateFromIssue;

    /**
     * 每个共识轮中回退多少个块是选举下一轮验证人的时机
     */
    private BigInteger electionBackwardBlockCount;

    /**
     * 10年内基金会向激励池填充额度(ATP)
     */
    private Map<Integer, BigDecimal> foundationSubsidies;

    /**
     * 提案url参数模板
     */
    private String proposalUrlTemplate;

    /**
     * 提案pip_num参数模板
     */
    private String proposalPipNumTemplate;

    /**
     * keyBase
     */
    private String keyBase;

    /**
     * keyBaseApi
     */
    private String keyBaseApi;

    /**
     * 初始内置节点默认质押金额(VON)
     */
    private BigDecimal defaultStakingLockedAmount;

    /**
     * 【违规-低出块率】最大容忍周期数内连续零出块的次数
     * 说明：代表零出块次数阈值，当节点持续零出块达成时长且次数大于等于该阈值则会受到处罚
     */
    private Integer zeroProduceNumberThreshold;

    /**
     * 【违规-低出块率】保持零出块最大容忍的共识轮数
     * 说明：用N代表下面字段所设置的值，阐述如下：
     * 上一次零出块后，在往后的N个共识周期内如若再出现零出块，则在这N个共识周期完成时记录零出块信息
     */
    private Integer zeroProduceCumulativeTime;

    /**
     * 【违规-低出块率】零出块处罚锁定周期数
     */
    private Integer zeroProduceFreezeDuration;

    /**
     * 节点委托奖励比例的修改幅度/节点的委托比例调整幅度，需要除以100%
     */
    private Integer rewardPerMaxChangeRange;

    /**
     * 节点委托奖励比例的修改间隔/调整委托比例的间隔周期
     */
    private Integer rewardPerChangeInterval;

    /**
     * 初始内置节点信息
     */
    private List<CustomStaking> defaultStakingList = new ArrayList<>();

    /**
     * 代币定义事件
     */
    private Map<String, String> eventDefine;

    // 地址前缀
    private String addressPrefix;

    @PostConstruct
    public void init() throws ConfigLoadingException {

        BlockChainConfig.INNER_CONTRACT_ADDR = new HashSet<>(InnerContractAddrEnum.getAddresses());
        defaultStakingLockedAmount = Convert.toVon(defaultStakingLockedAmount, Convert.Unit.KPVON);
        // 使用经济模型参数接口返回的数据更新配置
        updateWithEconomicConfig(client.getEconomicConfig());
        // 刷新合约
        client.updateContract();
    }

    private void updateWithEconomicConfig(EconomicConfig dec) {
        //【通用】每个验证人每个共识周期出块数量目标值
        setExpectBlockCount(dec.getCommon().getPerRoundBlocks());
        //【通用】每个共识轮验证节点数量
        setConsensusValidatorCount(dec.getCommon().getMaxConsensusVals());
        //【通用】每个结算周期验证节点数量
        setSettlementValidatorCount(dec.getStaking().getMaxValidators());
        //【通用】增发周期规定的分钟数
        setAdditionalCycleMinutes(dec.getCommon().getAdditionalCycleTime());
        //【通用】每个结算周期的分钟数
        setSettlementCycleMinutes(dec.getCommon().getMaxEpochMinutes());
        //【通用】出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
        setBlockInterval(dec.getCommon().getNodeBlockTimeWindow().divide(expectBlockCount));
        //【通用】共识轮区块数 = expectBlockCount x consensusValidatorCount
        setConsensusPeriodBlockCount(expectBlockCount.multiply(dec.getCommon().getMaxConsensusVals()));
        //【通用】每个结算周期区块总数=ROUND_DOWN(结算周期规定的分钟数x60/(出块间隔x共识轮区块数))x共识轮区块数
        setSettlePeriodBlockCount(settlementCycleMinutes.multiply(BigInteger.valueOf(60))
                                                        .divide(blockInterval.multiply(consensusPeriodBlockCount))
                                                        .multiply(consensusPeriodBlockCount));
        //【通用】PlatOn基金会账户地址
        setPlatOnFundAccount(dec.getInnerAcc().getPlatonFundAccount());
        //【通用】PlatOn基金会账户初始余额
        setPlatOnFundInitAmount(new BigDecimal(dec.getInnerAcc().getPlatonFundBalance()));
        //【通用】社区开发者激励基金账户地址
        setCommunityFundAccount(dec.getInnerAcc().getCdfAccount());
        //【通用】社区开发者激励基金账户初始余额
        setCommunityFundInitAmount(new BigDecimal(dec.getInnerAcc().getCdfBalance()));

        //【质押】创建验证人最低的质押Token数(VON)
        setStakeThreshold(new BigDecimal(dec.getStaking().getStakeThreshold()));
        //【质押】委托人每次委托及赎回的最低Token数(VON)
        setDelegateThreshold(new BigDecimal(dec.getStaking().getOperatingThreshold()));
        //【质押】节点质押退回锁定的结算周期数
        setUnStakeRefundSettlePeriodCount(dec.getStaking().getUnStakeFreezeDuration());
        setUnDelegateFreezeDurationCount(dec.getStaking().getUnDelegateFreezeDuration());
        //【惩罚】双签奖励百分比
        setDuplicateSignRewardRate(dec.getSlashing()
                                      .getDuplicateSignReportReward()
                                      .divide(BigDecimal.valueOf(100), 2, RoundingMode.FLOOR));
        //【惩罚】双签处罚万分比
        setDuplicateSignSlashRate(new BigDecimal(dec.getSlashing()
                                                    .getSlashFractionDuplicateSign()).divide(BigDecimal.valueOf(10000),
                                                                                             16,
                                                                                             RoundingMode.FLOOR));
        //【惩罚】举报证据有效周期数
        setEvidenceValidEpoch(new BigDecimal(dec.getSlashing().getMaxEvidenceAge()));
        //【惩罚】扣除区块奖励的个数
        setSlashBlockRewardCount(new BigDecimal(dec.getSlashing().getSlashBlocksReward()));

        //【治理】文本提案参与率: >
        setMinProposalTextParticipationRate(dec.getGov()
                                               .getTextProposalVoteRate()
                                               .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //【治理】文本提案支持率：>=
        setMinProposalTextSupportRate(dec.getGov()
                                         .getTextProposalSupportRate()
                                         .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //【治理】取消提案参与率: >
        setMinProposalCancelParticipationRate(dec.getGov()
                                                 .getCancelProposalVoteRate()
                                                 .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //【治理】取消提案支持率：>=
        setMinProposalCancelSupportRate(dec.getGov()
                                           .getCancelProposalSupportRate()
                                           .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //【治理】升级提案通过率
        setMinProposalUpgradePassRate(dec.getGov()
                                         .getVersionProposalSupportRate()
                                         .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //【治理】文本提案投票周期
        setProposalTextConsensusRounds(new BigDecimal(dec.getGov()
                                                         .getTextProposalVoteDurationSeconds()) // 文本提案的投票持续最长的时间（单位：s）
                                                                                                .divide(new BigDecimal(
                                                                                                                BigInteger.ONE // 出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
                                                                                                                               .multiply(
                                                                                                                                       dec.getCommon()
                                                                                                                                          .getPerRoundBlocks())
                                                                                                                               .multiply(
                                                                                                                                       consensusValidatorCount))
                                                                                                        //每个共识轮验证节点数量
                                                                                                        ,
                                                                                                        0,
                                                                                                        RoundingMode.FLOOR));

        //【治理】参数提案的投票持续最长的时间（单位：s）
        setParamProposalVoteDurationSeconds(dec.getGov().getParamProposalVoteDurationSeconds());
        //【治理】参数提案投票参与率阈值（参数提案投票通过条件之一：大于此值，则参数提案投票通过)
        setParamProposalVoteRate(dec.getGov()
                                    .getParamProposalVoteRate()
                                    .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));
        //【治理】参数提案投票支持率阈值（参数提案投票通过条件之一：大于等于此值，则参数提案投票通过
        setParamProposalSupportRate(dec.getGov()
                                       .getParamProposalSupportRate()
                                       .divide(BigDecimal.valueOf(10000), 16, RoundingMode.FLOOR));

        //【奖励】激励池分配给出块激励的比例
        setBlockRewardRate(new BigDecimal(dec.getReward().getNewBlockRate()).divide(BigDecimal.valueOf(100),
                                                                                    2,
                                                                                    RoundingMode.FLOOR));
        //【奖励】激励池分配给质押激励的比例 = 1-区块奖励比例
        setStakeRewardRate(BigDecimal.ONE.subtract(blockRewardRate));
        //【奖励】Platon基金会年限
        setPlatOnFoundationYear(dec.getReward().getPlatonFoundationYear());
        //【惩罚】零出块次数
        setZeroProduceCumulativeTime(dec.getSlashing().getZeroProduceCumulativeTime().intValue());
        //【惩罚】零出块阈值
        setZeroProduceNumberThreshold(dec.getSlashing().getZeroProduceNumberThreshold().intValue());
        // 节点零出块惩罚被锁定时间
        setZeroProduceFreezeDuration(dec.getSlashing().getZeroProduceFreezeDuration().intValue());
        //【质押】委托比例调整幅度限制
        setRewardPerMaxChangeRange(dec.getStaking().getRewardPerMaxChangeRange().intValue());
        //【质押】委托比例调整间隔
        setRewardPerChangeInterval(dec.getStaking().getRewardPerChangeInterval().intValue());
        //【锁仓】最小锁仓释放金额,（debug_economic接口platon版本会返回minimumRelease，alaya版本不会返回minimumRelease
        // 此值在alaya版本浏览器需要在ParameterService.initConfigTable()中进行设置
        //setRestrictingMinimumRelease(new BigDecimal(dec.getRestricting().getMinimumRelease()));
    }

}
