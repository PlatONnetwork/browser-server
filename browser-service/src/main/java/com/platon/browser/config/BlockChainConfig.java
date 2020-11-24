package com.platon.browser.config;

import com.alaya.contracts.ppos.dto.resp.GovernParam;
import com.alaya.contracts.ppos.dto.resp.ParamItem;
import com.alaya.contracts.ppos.dto.resp.ParamValue;
import com.alaya.protocol.core.methods.response.bean.EconomicConfig;
import com.alaya.utils.Convert;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.enums.ModifiableGovernParamEnum;
import com.platon.browser.exception.ConfigLoadingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
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
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */

@Slf4j
@DependsOn("networkParms")
@Configuration
@ConfigurationProperties(prefix="platon")
public class BlockChainConfig {

    @Autowired
    private ConfigMapper configMapper;
    
    static {
        File saltFile = FileUtils.getFile(System.getProperty("user.dir"), "jasypt.properties");
        Properties properties = new Properties();
        try(InputStream in = new FileInputStream(saltFile)) {
            properties.load(in);
            String salt=properties.getProperty("jasypt.encryptor.password");
            if(StringUtils.isBlank(salt)) throw new ConfigLoadingException("加密盐不能为空!");
            salt=salt.trim();
            System.setProperty("JASYPT_ENCRYPTOR_PASSWORD",salt);
            log.info("salt:{}",salt);
        } catch (IOException | ConfigLoadingException e) {
            log.error("加载解密文件出错",e);
            System.exit(1);
        }
    }

    @Autowired
    private PlatOnClient client;

    private static Set<String> INNER_CONTRACT_ADDR ;

    public Set<String> getInnerContractAddr(){
        return Collections.unmodifiableSet(INNER_CONTRACT_ADDR);
    }

    /*******************以下参数通过rpc接口debug_economicConfig获取*******************/
    //【通用】默认每个区块的最大Gas
    @Value("${platon.maxBlockGasLimit}")
    private BigDecimal maxBlockGasLimit;
    //【通用】每个验证人每个共识周期出块数量目标值
    private BigInteger expectBlockCount;
    //【通用】每个共识轮验证节点数量
    private BigInteger consensusValidatorCount;
    //【通用】每个结算周期验证节点数量
    private BigInteger settlementValidatorCount;
    //【通用】每个增发周期的分钟数
    private BigInteger additionalCycleMinutes;
    //【通用】每个结算周期的分钟数
    private BigInteger settlementCycleMinutes;
    //【通用】每个增发周期内的结算周期数
    private BigInteger settlePeriodCountPerIssue;
    //【通用】出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
    private BigInteger blockInterval;
    //【通用】共识轮区块数 = expectBlockCount x consensusValidatorCount
    private BigInteger consensusPeriodBlockCount;
    //【通用】每个结算周期区块总数=ROUND_DOWN(结算周期规定的分钟数x60/(出块间隔x共识轮区块数))x共识轮区块数
    private BigInteger settlePeriodBlockCount;
    //【通用】每个增发周期区块总数=ROUND_DOWN(增发周期的时间x60/(出块间隔x结算周期区块数))x结算周期区块数
    private BigInteger addIssuePeriodBlockCount;
    //【通用】PlatOn基金会账户地址
    private String platOnFundAccount;
    //【通用】PlatOn基金会账户初始余额
    private BigDecimal platOnFundInitAmount;
    //【通用】开发者激励基金账户地址
    private String communityFundAccount;
    //【通用】开发者激励基金账户初始余额
    private BigDecimal communityFundInitAmount;

    //【质押】质押门槛: 创建验证人最低的质押Token数(VON)
    private BigDecimal stakeThreshold;
    //【质押】委托门槛(VON)
    private BigDecimal delegateThreshold;
    //【质押】节点质押退回锁定的结算周期数
    private BigInteger unStakeRefundSettlePeriodCount;

    //【惩罚】双签奖励百分比
    private BigDecimal duplicateSignRewardRate;
    //【惩罚】双签处罚百分比
    private BigDecimal duplicateSignSlashRate;
    //【惩罚】举报证据生效周期数
    private BigDecimal evidenceValidEpoch;
    //【惩罚】扣除区块奖励的个数
    private BigDecimal slashBlockRewardCount;

    //【治理】文本提案参与率: >
    private BigDecimal minProposalTextParticipationRate;
    //【治理】文本提案支持率：>=
    private BigDecimal minProposalTextSupportRate;
    //【治理】取消提案参与率: >
    private BigDecimal minProposalCancelParticipationRate;
    //【治理】取消提案支持率：>=
    private BigDecimal minProposalCancelSupportRate;
    //【治理】升级提案通过率
    private BigDecimal minProposalUpgradePassRate;
    //【治理】文本提案默认结束轮数
    private BigDecimal proposalTextConsensusRounds;
    //【治理】设置预升级开始轮数
    private BigDecimal versionProposalActiveConsensusRounds;
    //【治理】参数提案的投票持续最长的时间（单位：s）
    private BigInteger paramProposalVoteDurationSeconds;
    //【治理】参数提案投票参与率阈值（参数提案投票通过条件之一：大于此值，则参数提案投票通过)
    private BigDecimal paramProposalVoteRate;
    //【治理】参数提案投票支持率阈值（参数提案投票通过条件之一：大于等于此值，则参数提案投票通过
    private BigDecimal paramProposalSupportRate;

    //【奖励】激励池分配给出块激励的比例
    private BigDecimal blockRewardRate;
    //【奖励】激励池分配给质押激励的比例
    private BigDecimal stakeRewardRate;
    //【奖励】Platon基金会年限
    private BigInteger platOnFoundationYear;

    //【通用】当前增发周期轮数
    private BigDecimal issueEpochRound;
    //【通用】当前增发周期开始区块号
    private BigDecimal issueEpochStartBlockNumber;
    //【通用】当前增发周期结束区块号
    private BigDecimal issueEpochEndBlockNumber;

    /*******************以下参数通过从应用配置文件获取*******************/
    // 质押节点统计年化率最多取多少个连续周期
    private BigInteger maxSettlePeriodCount4AnnualizedRateStat;
    // PlatON初始总发行量(ATP)
    private BigDecimal initIssueAmount;
    // 每年固定增发比例
    private BigDecimal addIssueRate;
    // 每年增发分配给激励池的比例
    private BigDecimal incentiveRateFromIssue;
    //每个共识轮中回退多少个块是选举下一轮验证人的时机
    private BigInteger electionBackwardBlockCount;
    //10年内基金会向激励池填充额度(ATP)
    private Map<Integer,BigDecimal> foundationSubsidies;
    //提案url参数模板
    private String proposalUrlTemplate;
    //提案pip_num参数模板
    private String proposalPipNumTemplate;
    //keyBase
    private String keyBase;
    //keyBaseApi
    private String keyBaseApi;
    // 初始内置节点默认质押金额(VON)
    private BigDecimal defaultStakingLockedAmount;
    // 零出块次数阈值，在指定时间范围内达到该次数则处罚
    private Integer zeroProduceNumberThreshold;
    // 说明：用N代表下面字段所设置的值，阐述如下：
    // 上一次零出块后，在往后的N个共识周期内如若再出现零出块，则在这N个共识周期完成时记录零出块信息
    private Integer zeroProduceCumulativeTime;
    // 节点零出块惩罚被锁定时间
    private Integer zeroProduceFreezeDuration;
    // 节点的委托比例调整幅度，需要除以100%
    private Integer rewardPerMaxChangeRange;
    // 调整委托比例的间隔周期
    private Integer rewardPerChangeInterval;
    // 初始内置节点信息
    private List<CustomStaking> defaultStakingList=new ArrayList<>();

    // 代币定义事件
    private Map<String, String> eventDefine;

    @PostConstruct
    public void init() throws ConfigLoadingException {
    	BlockChainConfig.INNER_CONTRACT_ADDR = new HashSet<>(InnerContractAddrEnum.getAddresses());
        defaultStakingLockedAmount= Convert.toVon(defaultStakingLockedAmount, Convert.Unit.ATP);
        updateWithEconomicConfig(client.getEconomicConfig());
//        updateWithGovernParams(client.getGovernParamValue(""));
    }

    private void updateWithGovernParams(List<GovernParam> governParam) {
        governParam.forEach(param->{
            ParamItem pi = param.getParamItem();
            ParamValue pv = param.getParamValue();
            ModifiableGovernParamEnum paramEnum = ModifiableGovernParamEnum.getMap().get(pi.getName());
            switch (paramEnum){
                case ZERO_PRODUCE_NUMBER_THRESHOLD:
                    this.setZeroProduceNumberThreshold(Integer.valueOf(pv.getValue()));
                    break;
                // 上一次零出块后，在往后的N个共识周期内如若再出现零出块，则在这N个共识周期完成时记录零出块信息
                case ZERO_PRODUCE_CUMULATIVE_TIME:
                    this.setZeroProduceCumulativeTime(Integer.valueOf(pv.getValue()));
                    break;
                default:
                    break;
            }
        });
    }

    private void updateWithEconomicConfig(EconomicConfig dec) {
        //【通用】每个验证人每个共识周期出块数量目标值
        this.setExpectBlockCount(dec.getCommon().getPerRoundBlocks());
        //【通用】每个共识轮验证节点数量
        this.setConsensusValidatorCount(dec.getCommon().getMaxConsensusVals());
        //【通用】每个结算周期验证节点数量
        this.setSettlementValidatorCount(dec.getStaking().getMaxValidators());
        //【通用】增发周期规定的分钟数
        this.setAdditionalCycleMinutes(dec.getCommon().getAdditionalCycleTime());
        //【通用】每个结算周期的分钟数
        this.setSettlementCycleMinutes(dec.getCommon().getMaxEpochMinutes());
        //【通用】出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
        this.setBlockInterval(dec.getCommon().getNodeBlockTimeWindow().divide(this.expectBlockCount));
        //【通用】共识轮区块数 = expectBlockCount x consensusValidatorCount
        this.setConsensusPeriodBlockCount(this.expectBlockCount.multiply(dec.getCommon().getMaxConsensusVals()));
        //【通用】每个结算周期区块总数=ROUND_DOWN(结算周期规定的分钟数x60/(出块间隔x共识轮区块数))x共识轮区块数
        this.setSettlePeriodBlockCount(settlementCycleMinutes
                .multiply(BigInteger.valueOf(60))
                .divide(this.blockInterval.multiply(this.consensusPeriodBlockCount))
                .multiply(this.consensusPeriodBlockCount));
        //【通用】PlatOn基金会账户地址
        this.setPlatOnFundAccount(dec.getInnerAcc().getPlatonFundAccount());
        //【通用】PlatOn基金会账户初始余额
        this.setPlatOnFundInitAmount(new BigDecimal(dec.getInnerAcc().getPlatonFundBalance()));
        //【通用】社区开发者激励基金账户地址
        this.setCommunityFundAccount(dec.getInnerAcc().getCdfAccount());
        //【通用】社区开发者激励基金账户初始余额
        this.setCommunityFundInitAmount(new BigDecimal(dec.getInnerAcc().getCdfBalance()));

        //【质押】创建验证人最低的质押Token数(VON)
        this.setStakeThreshold(new BigDecimal(dec.getStaking().getStakeThreshold()));
        //【质押】委托人每次委托及赎回的最低Token数(VON)
        this.setDelegateThreshold(new BigDecimal(dec.getStaking().getOperatingThreshold()));
        //【质押】节点质押退回锁定的结算周期数
        this.setUnStakeRefundSettlePeriodCount(dec.getStaking().getUnStakeFreezeDuration());
        //【惩罚】双签奖励百分比
        this.setDuplicateSignRewardRate(dec.getSlashing().getDuplicateSignReportReward().divide(BigDecimal.valueOf(100),2,RoundingMode.FLOOR));
        //【惩罚】双签处罚万分比
        this.setDuplicateSignSlashRate(new BigDecimal(dec.getSlashing().getSlashFractionDuplicateSign()).divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【惩罚】举报证据有效周期数
        this.setEvidenceValidEpoch(new BigDecimal(dec.getSlashing().getMaxEvidenceAge()));
        //【惩罚】扣除区块奖励的个数
        this.setSlashBlockRewardCount(new BigDecimal(dec.getSlashing().getSlashBlocksReward()));

        //【治理】文本提案参与率: >
        this.setMinProposalTextParticipationRate(dec.getGov().getTextProposalVoteRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【治理】文本提案支持率：>=
        this.setMinProposalTextSupportRate(dec.getGov().getTextProposalSupportRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【治理】取消提案参与率: >
        this.setMinProposalCancelParticipationRate(dec.getGov().getCancelProposalVoteRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【治理】取消提案支持率：>=
        this.setMinProposalCancelSupportRate(dec.getGov().getCancelProposalSupportRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【治理】升级提案通过率
        this.setMinProposalUpgradePassRate(dec.getGov().getVersionProposalSupportRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【治理】文本提案投票周期
        this.setProposalTextConsensusRounds(new BigDecimal(dec.getGov().getTextProposalVoteDurationSeconds()) // 文本提案的投票持续最长的时间（单位：s）
                .divide(
                        new BigDecimal(
                                BigInteger.ONE // 出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
                                        .multiply(dec.getCommon().getPerRoundBlocks())
                                        .multiply(this.consensusValidatorCount)) //每个共识轮验证节点数量
                        ,0,RoundingMode.FLOOR
                ));

        //【治理】参数提案的投票持续最长的时间（单位：s）
        this.setParamProposalVoteDurationSeconds(dec.getGov().getParamProposalVoteDurationSeconds());
        //【治理】参数提案投票参与率阈值（参数提案投票通过条件之一：大于此值，则参数提案投票通过)
        this.setParamProposalVoteRate(dec.getGov().getParamProposalVoteRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));
        //【治理】参数提案投票支持率阈值（参数提案投票通过条件之一：大于等于此值，则参数提案投票通过
        this.setParamProposalSupportRate(dec.getGov().getParamProposalSupportRate().divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR));

        //【奖励】激励池分配给出块激励的比例
        this.setBlockRewardRate(new BigDecimal(dec.getReward().getNewBlockRate()).divide(BigDecimal.valueOf(100),2,RoundingMode.FLOOR));
        //【奖励】激励池分配给质押激励的比例 = 1-区块奖励比例
        this.setStakeRewardRate(BigDecimal.ONE.subtract(this.blockRewardRate));
        //【奖励】Platon基金会年限
        this.setPlatOnFoundationYear(dec.getReward().getPlatonFoundationYear());
        //【惩罚】零出块次数
        this.setZeroProduceCumulativeTime(dec.getSlashing().getZeroProduceCumulativeTime().intValue());
        //【惩罚】零出块阈值
        this.setZeroProduceNumberThreshold(dec.getSlashing().getZeroProduceNumberThreshold().intValue());
        // 节点零出块惩罚被锁定时间
        this.setZeroProduceFreezeDuration(dec.getSlashing().getZeroProduceFreezeDuration().intValue());
        //【质押】委托比例调整幅度限制
        this.setRewardPerMaxChangeRange(dec.getStaking().getRewardPerMaxChangeRange().intValue());
        //【质押】委托比例调整间隔
        this.setRewardPerChangeInterval(dec.getStaking().getRewardPerChangeInterval().intValue());
    }

    public ConfigMapper getConfigMapper () {
        return configMapper;
    }

    public void setConfigMapper ( ConfigMapper configMapper ) {
        this.configMapper = configMapper;
    }

    public PlatOnClient getClient () {
        return client;
    }

    public void setClient ( PlatOnClient client ) {
        this.client = client;
    }

    public BigDecimal getMaxBlockGasLimit () {
        return maxBlockGasLimit;
    }

    public void setMaxBlockGasLimit ( BigDecimal maxBlockGasLimit ) {
        this.maxBlockGasLimit = maxBlockGasLimit;
    }

    public BigInteger getExpectBlockCount () {
        return expectBlockCount;
    }

    public void setExpectBlockCount ( BigInteger expectBlockCount ) {
        this.expectBlockCount = expectBlockCount;
    }

    public BigInteger getConsensusValidatorCount () {
        return consensusValidatorCount;
    }

    public void setConsensusValidatorCount ( BigInteger consensusValidatorCount ) {
        this.consensusValidatorCount = consensusValidatorCount;
    }

    public BigInteger getSettlementValidatorCount () {
        return settlementValidatorCount;
    }

    public void setSettlementValidatorCount ( BigInteger settlementValidatorCount ) {
        this.settlementValidatorCount = settlementValidatorCount;
    }

    public BigInteger getAdditionalCycleMinutes () {
        return additionalCycleMinutes;
    }

    public void setAdditionalCycleMinutes ( BigInteger additionalCycleMinutes ) {
        this.additionalCycleMinutes = additionalCycleMinutes;
    }

    public BigInteger getSettlementCycleMinutes () {
        return settlementCycleMinutes;
    }

    public void setSettlementCycleMinutes ( BigInteger settlementCycleMinutes ) {
        this.settlementCycleMinutes = settlementCycleMinutes;
    }

    public BigInteger getSettlePeriodCountPerIssue () {
        return settlePeriodCountPerIssue;
    }

    public void setSettlePeriodCountPerIssue ( BigInteger settlePeriodCountPerIssue ) {
        this.settlePeriodCountPerIssue = settlePeriodCountPerIssue;
    }

    public BigInteger getBlockInterval () {
        return blockInterval;
    }

    public void setBlockInterval ( BigInteger blockInterval ) {
        this.blockInterval = blockInterval;
    }

    public BigInteger getConsensusPeriodBlockCount () {
        return consensusPeriodBlockCount;
    }

    public void setConsensusPeriodBlockCount ( BigInteger consensusPeriodBlockCount ) {
        this.consensusPeriodBlockCount = consensusPeriodBlockCount;
    }

    public BigInteger getSettlePeriodBlockCount () {
        return settlePeriodBlockCount;
    }

    public void setSettlePeriodBlockCount ( BigInteger settlePeriodBlockCount ) {
        this.settlePeriodBlockCount = settlePeriodBlockCount;
    }

    public BigInteger getAddIssuePeriodBlockCount () {
        return addIssuePeriodBlockCount;
    }

    public void setAddIssuePeriodBlockCount ( BigInteger addIssuePeriodBlockCount ) {
        this.addIssuePeriodBlockCount = addIssuePeriodBlockCount;
    }

    public String getPlatOnFundAccount () {
        return platOnFundAccount;
    }

    public void setPlatOnFundAccount ( String platOnFundAccount ) {
        this.platOnFundAccount = platOnFundAccount;
    }

    public BigDecimal getPlatOnFundInitAmount () {
        return platOnFundInitAmount;
    }

    public void setPlatOnFundInitAmount ( BigDecimal platOnFundInitAmount ) {
        this.platOnFundInitAmount = platOnFundInitAmount;
    }

    public String getCommunityFundAccount () {
        return communityFundAccount;
    }

    public void setCommunityFundAccount ( String communityFundAccount ) {
        this.communityFundAccount = communityFundAccount;
    }

    public BigDecimal getCommunityFundInitAmount () {
        return communityFundInitAmount;
    }

    public void setCommunityFundInitAmount ( BigDecimal communityFundInitAmount ) {
        this.communityFundInitAmount = communityFundInitAmount;
    }

    public BigDecimal getStakeThreshold () {
        return stakeThreshold;
    }

    public void setStakeThreshold ( BigDecimal stakeThreshold ) {
        this.stakeThreshold = stakeThreshold;
    }

    public BigDecimal getDelegateThreshold () {
        return delegateThreshold;
    }

    public void setDelegateThreshold ( BigDecimal delegateThreshold ) {
        this.delegateThreshold = delegateThreshold;
    }

    public BigInteger getUnStakeRefundSettlePeriodCount () {
        return unStakeRefundSettlePeriodCount;
    }

    public void setUnStakeRefundSettlePeriodCount ( BigInteger unStakeRefundSettlePeriodCount ) {
        this.unStakeRefundSettlePeriodCount = unStakeRefundSettlePeriodCount;
    }

    public BigDecimal getDuplicateSignRewardRate () {
        return duplicateSignRewardRate;
    }

    public void setDuplicateSignRewardRate ( BigDecimal duplicateSignRewardRate ) {
        this.duplicateSignRewardRate = duplicateSignRewardRate;
    }

    public BigDecimal getDuplicateSignSlashRate () {
        return duplicateSignSlashRate;
    }

    public void setDuplicateSignSlashRate ( BigDecimal duplicateSignSlashRate ) {
        this.duplicateSignSlashRate = duplicateSignSlashRate;
    }

    public BigDecimal getEvidenceValidEpoch () {
        return evidenceValidEpoch;
    }

    public void setEvidenceValidEpoch ( BigDecimal evidenceValidEpoch ) {
        this.evidenceValidEpoch = evidenceValidEpoch;
    }

    public BigDecimal getSlashBlockRewardCount () {
        return slashBlockRewardCount;
    }

    public void setSlashBlockRewardCount ( BigDecimal slashBlockRewardCount ) {
        this.slashBlockRewardCount = slashBlockRewardCount;
    }

    public BigDecimal getMinProposalTextParticipationRate () {
        return minProposalTextParticipationRate;
    }

    public void setMinProposalTextParticipationRate ( BigDecimal minProposalTextParticipationRate ) {
        this.minProposalTextParticipationRate = minProposalTextParticipationRate;
    }

    public BigDecimal getMinProposalTextSupportRate () {
        return minProposalTextSupportRate;
    }

    public void setMinProposalTextSupportRate ( BigDecimal minProposalTextSupportRate ) {
        this.minProposalTextSupportRate = minProposalTextSupportRate;
    }

    public BigDecimal getMinProposalCancelParticipationRate () {
        return minProposalCancelParticipationRate;
    }

    public void setMinProposalCancelParticipationRate ( BigDecimal minProposalCancelParticipationRate ) {
        this.minProposalCancelParticipationRate = minProposalCancelParticipationRate;
    }

    public BigDecimal getMinProposalCancelSupportRate () {
        return minProposalCancelSupportRate;
    }

    public void setMinProposalCancelSupportRate ( BigDecimal minProposalCancelSupportRate ) {
        this.minProposalCancelSupportRate = minProposalCancelSupportRate;
    }

    public BigDecimal getMinProposalUpgradePassRate () {
        return minProposalUpgradePassRate;
    }

    public void setMinProposalUpgradePassRate ( BigDecimal minProposalUpgradePassRate ) {
        this.minProposalUpgradePassRate = minProposalUpgradePassRate;
    }

    public BigDecimal getProposalTextConsensusRounds () {
        return proposalTextConsensusRounds;
    }

    public void setProposalTextConsensusRounds ( BigDecimal proposalTextConsensusRounds ) {
        this.proposalTextConsensusRounds = proposalTextConsensusRounds;
    }

    public BigDecimal getVersionProposalActiveConsensusRounds () {
        return versionProposalActiveConsensusRounds;
    }

    public void setVersionProposalActiveConsensusRounds ( BigDecimal versionProposalActiveConsensusRounds ) {
        this.versionProposalActiveConsensusRounds = versionProposalActiveConsensusRounds;
    }

    public BigInteger getParamProposalVoteDurationSeconds () {
        return paramProposalVoteDurationSeconds;
    }

    public void setParamProposalVoteDurationSeconds ( BigInteger paramProposalVoteDurationSeconds ) {
        this.paramProposalVoteDurationSeconds = paramProposalVoteDurationSeconds;
    }

    public BigDecimal getParamProposalVoteRate () {
        return paramProposalVoteRate;
    }

    public void setParamProposalVoteRate ( BigDecimal paramProposalVoteRate ) {
        this.paramProposalVoteRate = paramProposalVoteRate;
    }

    public BigDecimal getParamProposalSupportRate () {
        return paramProposalSupportRate;
    }

    public void setParamProposalSupportRate ( BigDecimal paramProposalSupportRate ) {
        this.paramProposalSupportRate = paramProposalSupportRate;
    }

    public BigDecimal getBlockRewardRate () {
        return blockRewardRate;
    }

    public void setBlockRewardRate ( BigDecimal blockRewardRate ) {
        this.blockRewardRate = blockRewardRate;
    }

    public BigDecimal getStakeRewardRate () {
        return stakeRewardRate;
    }

    public void setStakeRewardRate ( BigDecimal stakeRewardRate ) {
        this.stakeRewardRate = stakeRewardRate;
    }

    public BigInteger getPlatOnFoundationYear () {
        return platOnFoundationYear;
    }

    public void setPlatOnFoundationYear ( BigInteger platOnFoundationYear ) {
        this.platOnFoundationYear = platOnFoundationYear;
    }

    public BigDecimal getIssueEpochRound () {
        return issueEpochRound;
    }

    public void setIssueEpochRound ( BigDecimal issueEpochRound ) {
        this.issueEpochRound = issueEpochRound;
    }

    public BigDecimal getIssueEpochStartBlockNumber () {
        return issueEpochStartBlockNumber;
    }

    public void setIssueEpochStartBlockNumber ( BigDecimal issueEpochStartBlockNumber ) {
        this.issueEpochStartBlockNumber = issueEpochStartBlockNumber;
    }

    public BigDecimal getIssueEpochEndBlockNumber () {
        return issueEpochEndBlockNumber;
    }

    public void setIssueEpochEndBlockNumber ( BigDecimal issueEpochEndBlockNumber ) {
        this.issueEpochEndBlockNumber = issueEpochEndBlockNumber;
    }

    public BigInteger getMaxSettlePeriodCount4AnnualizedRateStat () {
        return maxSettlePeriodCount4AnnualizedRateStat;
    }

    public void setMaxSettlePeriodCount4AnnualizedRateStat ( BigInteger maxSettlePeriodCount4AnnualizedRateStat ) {
        this.maxSettlePeriodCount4AnnualizedRateStat = maxSettlePeriodCount4AnnualizedRateStat;
    }

    public BigDecimal getInitIssueAmount () {
        return initIssueAmount;
    }

    public void setInitIssueAmount ( BigDecimal initIssueAmount ) {
        this.initIssueAmount = initIssueAmount;
    }

    public BigDecimal getAddIssueRate () {
        return addIssueRate;
    }

    public void setAddIssueRate ( BigDecimal addIssueRate ) {
        this.addIssueRate = addIssueRate;
    }

    public BigDecimal getIncentiveRateFromIssue () {
        return incentiveRateFromIssue;
    }

    public void setIncentiveRateFromIssue ( BigDecimal incentiveRateFromIssue ) {
        this.incentiveRateFromIssue = incentiveRateFromIssue;
    }

    public BigInteger getElectionBackwardBlockCount () {
        return electionBackwardBlockCount;
    }

    public void setElectionBackwardBlockCount ( BigInteger electionBackwardBlockCount ) {
        this.electionBackwardBlockCount = electionBackwardBlockCount;
    }

    public Map <Integer, BigDecimal> getFoundationSubsidies () {
        return foundationSubsidies;
    }

    public void setFoundationSubsidies ( Map <Integer, BigDecimal> foundationSubsidies ) {
        this.foundationSubsidies = foundationSubsidies;
    }

    public String getProposalUrlTemplate () {
        return proposalUrlTemplate;
    }

    public void setProposalUrlTemplate ( String proposalUrlTemplate ) {
        this.proposalUrlTemplate = proposalUrlTemplate;
    }

    public String getProposalPipNumTemplate () {
        return proposalPipNumTemplate;
    }

    public void setProposalPipNumTemplate ( String proposalPipNumTemplate ) {
        this.proposalPipNumTemplate = proposalPipNumTemplate;
    }

    public String getKeyBase () {
        return keyBase;
    }

    public void setKeyBase ( String keyBase ) {
        this.keyBase = keyBase;
    }

    public String getKeyBaseApi () {
        return keyBaseApi;
    }

    public void setKeyBaseApi ( String keyBaseApi ) {
        this.keyBaseApi = keyBaseApi;
    }

    public BigDecimal getDefaultStakingLockedAmount () {
        return defaultStakingLockedAmount;
    }

    public void setDefaultStakingLockedAmount ( BigDecimal defaultStakingLockedAmount ) {
        this.defaultStakingLockedAmount = defaultStakingLockedAmount;
    }

    public Integer getZeroProduceNumberThreshold() {
        return zeroProduceNumberThreshold;
    }

    public void setZeroProduceNumberThreshold(Integer zeroProduceNumberThreshold) {
        this.zeroProduceNumberThreshold = zeroProduceNumberThreshold;
    }

    public Integer getZeroProduceCumulativeTime() {
        return zeroProduceCumulativeTime;
    }

    public void setZeroProduceCumulativeTime(Integer zeroProduceCumulativeTime) {
        this.zeroProduceCumulativeTime = zeroProduceCumulativeTime;
    }

    public Integer getZeroProduceFreezeDuration() {
        return zeroProduceFreezeDuration;
    }

    public void setZeroProduceFreezeDuration(Integer zeroProduceFreezeDuration) {
        this.zeroProduceFreezeDuration = zeroProduceFreezeDuration;
    }

    public Integer getRewardPerMaxChangeRange() {
		return rewardPerMaxChangeRange;
	}

	public void setRewardPerMaxChangeRange(Integer rewardPerMaxChangeRange) {
		this.rewardPerMaxChangeRange = rewardPerMaxChangeRange;
	}

	public Integer getRewardPerChangeInterval() {
		return rewardPerChangeInterval;
	}

	public void setRewardPerChangeInterval(Integer rewardPerChangeInterval) {
		this.rewardPerChangeInterval = rewardPerChangeInterval;
	}

	public List <CustomStaking> getDefaultStakingList () {
        return defaultStakingList;
    }

    public void setDefaultStakingList ( List <CustomStaking> defaultStakingList ) {
        this.defaultStakingList = defaultStakingList;
    }

	public static Set<String> getINNER_CONTRACT_ADDR() {
		return INNER_CONTRACT_ADDR;
	}

	public static void setINNER_CONTRACT_ADDR(Set<String> iNNER_CONTRACT_ADDR) {
		INNER_CONTRACT_ADDR = iNNER_CONTRACT_ADDR;
	}

	
    public Map<String, String> getEventDefine() {
        return eventDefine;
    }
    public void setEventDefine(Map<String, String> eventDefine) {
        this.eventDefine = eventDefine;
    }
}