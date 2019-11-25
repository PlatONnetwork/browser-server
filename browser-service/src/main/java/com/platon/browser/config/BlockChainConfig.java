package com.platon.browser.config;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.govern.ModifiableParam;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.mapper.ConfigMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.ConfigLoadingException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.web3j.platon.bean.EconomicConfig;
import org.web3j.utils.Convert;

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
@Data
@Slf4j
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

    private static final Set<String> INNER_CONTRACT_ADDR = new HashSet<>(InnerContractAddrEnum.getAddresses());

    public Set<String> getInnerContractAddr(){
        return Collections.unmodifiableSet(INNER_CONTRACT_ADDR);
    }

    /*******************以下参数通过rpc接口debug_economicConfig获取*******************/
    //【通用】默认每个区块的最大Gas
    private BigDecimal maxBlockGasLimit=BigDecimal.ZERO;
    //【通用】每个验证人每个共识周期出块数量目标值
    private BigInteger expectBlockCount;
    //【通用】每个共识轮验证节点数量
    private BigInteger consensusValidatorCount;
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

    //【质押】质押门槛: 创建验证人最低的质押Token数(LAT)
    private BigDecimal stakeThreshold;
    //【质押】委托门槛(LAT)
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

    /*******************以下参数通过从应用配置文件获取*******************/
    // 质押节点统计年化率最多取多少个连续周期
    private BigInteger maxSettlePeriodCount4AnnualizedRateStat;
    // PlatON初始总发行量(LAT)
    private BigDecimal initIssueAmount;
    // 每年固定增发比例
    private BigDecimal addIssueRate;
    // 每年增发分配给激励池的比例
    private BigDecimal incentiveRateFromIssue;
    //每个共识轮中回退多少个块是选举下一轮验证人的时机
    private BigInteger electionBackwardBlockCount;
    //10年内基金会向激励池填充额度(LAT)
    private Map<Integer,BigDecimal> foundationSubsidies;
    //提案url参数模板
    private String proposalUrlTemplate;
    //提案pip_num参数模板
    private String proposalPipNumTemplate;
    //keyBase
    private String keyBase;
    //keyBaseApi
    private String keyBaseApi;
    // 初始内置节点默认质押金额(LAT)
    private BigDecimal defaultStakingLockedAmount;
    // 初始内置节点信息
    private List<CustomStaking> defaultStakingList=new ArrayList<>();

    @PostConstruct
    public void init() throws ConfigLoadingException {
        updateWithEconomicConfig(client.getEconomicConfig());
        // 使用数据库config表的配置覆盖当前配置
        List<Config> configList = configMapper.selectByExample(null);
        ModifiableParam modifiableParam = ModifiableParam.builder().build().init(configList);
        updateWithModifiableParam(modifiableParam);
    }

    private void updateWithEconomicConfig(EconomicConfig dec) {
        //【通用】每个验证人每个共识周期出块数量目标值
        this.expectBlockCount=dec.getCommon().getPerRoundBlocks();
        //【通用】每个共识轮验证节点数量
        this.consensusValidatorCount=dec.getCommon().getMaxConsensusVals();
        //【通用】增发周期规定的分钟数
        this.additionalCycleMinutes=dec.getCommon().getAdditionalCycleTime();
        //【通用】每个结算周期的分钟数
        this.settlementCycleMinutes=dec.getCommon().getMaxEpochMinutes();
        //【通用】出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
        this.blockInterval=dec.getCommon().getNodeBlockTimeWindow().divide(this.expectBlockCount);
        //【通用】共识轮区块数 = expectBlockCount x consensusValidatorCount
        this.consensusPeriodBlockCount=this.expectBlockCount.multiply(dec.getCommon().getMaxConsensusVals());
        //【通用】每个结算周期区块总数=ROUND_DOWN(结算周期规定的分钟数x60/(出块间隔x共识轮区块数))x共识轮区块数
        this.settlePeriodBlockCount=settlementCycleMinutes
                .multiply(BigInteger.valueOf(60))
                .divide(this.blockInterval.multiply(this.consensusPeriodBlockCount))
                .multiply(this.consensusPeriodBlockCount);
        //【通用】每个增发周期区块总数=ROUND_DOWN(增发周期的时间x60/(出块间隔x结算周期区块数))x结算周期区块数
        this.addIssuePeriodBlockCount=this.additionalCycleMinutes
                .multiply(BigInteger.valueOf(60))
                .divide(this.blockInterval.multiply(this.settlePeriodBlockCount))
                .multiply(this.settlePeriodBlockCount);
        //【通用】每个增发周期内的结算周期数=增发周期区块数/结算周期区块数
        this.settlePeriodCountPerIssue=this.addIssuePeriodBlockCount.divide(this.settlePeriodBlockCount);
        //【通用】PlatOn基金会账户地址
        this.platOnFundAccount=dec.getInnerAcc().getPlatonFundAccount();
        //【通用】PlatOn基金会账户初始余额
        this.platOnFundInitAmount=new BigDecimal(dec.getInnerAcc().getPlatonFundBalance());
        //【通用】社区开发者激励基金账户地址
        this.communityFundAccount=dec.getInnerAcc().getCdfAccount();
        //【通用】社区开发者激励基金账户初始余额
        this.communityFundInitAmount=new BigDecimal(dec.getInnerAcc().getCdfBalance());

        //【质押】创建验证人最低的质押Token数(LAT)
        this.stakeThreshold= Convert.fromVon(new BigDecimal(dec.getStaking().getStakeThreshold()), Convert.Unit.LAT);
        //【质押】委托人每次委托及赎回的最低Token数(LAT)
        this.delegateThreshold=Convert.fromVon(new BigDecimal(dec.getStaking().getOperatingThreshold()), Convert.Unit.LAT);
        //【质押】节点质押退回锁定的结算周期数
        this.unStakeRefundSettlePeriodCount=dec.getStaking().getUnStakeFreezeDuration();
        //【惩罚】双签奖励百分比
        this.duplicateSignRewardRate=dec.getSlashing().getDuplicateSignReportReward().divide(BigDecimal.valueOf(100),2,RoundingMode.FLOOR);
        //【惩罚】双签处罚万分比
        this.duplicateSignSlashRate=new BigDecimal(dec.getSlashing().getSlashFractionDuplicateSign()).divide(BigDecimal.valueOf(10000),16, RoundingMode.FLOOR);
        //【惩罚】举报证据有效周期数
        this.evidenceValidEpoch=new BigDecimal(dec.getSlashing().getMaxEvidenceAge());

        //【治理】文本提案参与率: >
        this.minProposalTextParticipationRate=dec.getGov().getTextProposalVoteRate();
        //【治理】文本提案支持率：>=
        this.minProposalTextSupportRate=dec.getGov().getTextProposalSupportRate();
        //【治理】取消提案参与率: >
        this.minProposalCancelParticipationRate=dec.getGov().getCancelProposalVoteRate();
        //【治理】取消提案支持率：>=
        this.minProposalCancelSupportRate=dec.getGov().getCancelProposalSupportRate();
        //【治理】升级提案通过率
        this.minProposalUpgradePassRate=dec.getGov().getVersionProposalSupportRate();
        //【治理】文本提案投票周期
        this.proposalTextConsensusRounds=new BigDecimal(dec.getGov().getTextProposalVoteDurationSeconds()) // 文本提案的投票持续最长的时间（单位：s）
                .divide(
                        new BigDecimal(
                                this.blockInterval // 出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
                                        .multiply(dec.getCommon().getPerRoundBlocks())
                                        .multiply(this.consensusValidatorCount)) //每个共识轮验证节点数量
                        ,0,RoundingMode.FLOOR
                );

        //【治理】参数提案的投票持续最长的时间（单位：s）
        this.paramProposalVoteDurationSeconds=dec.getGov().getParamProposalVoteDurationSeconds();
        //【治理】参数提案投票参与率阈值（参数提案投票通过条件之一：大于此值，则参数提案投票通过)
        this.paramProposalVoteRate=dec.getGov().getParamProposalVoteRate();
        //【治理】参数提案投票支持率阈值（参数提案投票通过条件之一：大于等于此值，则参数提案投票通过
        this.paramProposalSupportRate=dec.getGov().getParamProposalSupportRate();

        //【奖励】激励池分配给出块激励的比例
        this.blockRewardRate=new BigDecimal(dec.getReward().getNewBlockRate()).divide(BigDecimal.valueOf(100),2,RoundingMode.FLOOR);
        //【奖励】激励池分配给质押激励的比例 = 1-区块奖励比例
        this.stakeRewardRate=BigDecimal.ONE.subtract(this.blockRewardRate);
        //【奖励】Platon基金会年限
        this.platOnFoundationYear=dec.getReward().getPlatonFoundationYear();
    }
    
    public void updateWithModifiableParam(ModifiableParam modifiableParam){
        //创建验证人最低的质押Token数(K)
        this.stakeThreshold=modifiableParam.getStaking().getStakeThreshold();
        //委托人每次委托及赎回的最低Token数(H)
        this.delegateThreshold=modifiableParam.getStaking().getOperatingThreshold();
        //节点质押退回锁定周期
        this.unStakeRefundSettlePeriodCount=modifiableParam.getStaking().getUnStakeFreezeDuration().toBigInteger();
        //备选验证节点数量(U)
        this.consensusValidatorCount=modifiableParam.getStaking().getMaxValidators().toBigInteger();
        //举报最高处罚n3‱
        this.duplicateSignSlashRate=modifiableParam.getSlashing().getSlashFractionDuplicateSign().divide(BigDecimal.valueOf(10000),16,RoundingMode.FLOOR);
        //举报奖励n4%
        this.duplicateSignRewardRate=modifiableParam.getSlashing().getDuplicateSignReportReward().divide(BigDecimal.valueOf(100),2,RoundingMode.FLOOR);;
        //证据有效期
        this.evidenceValidEpoch=modifiableParam.getSlashing().getMaxEvidenceAge();
        //扣除区块奖励的个数
        this.slashBlockRewardCount=modifiableParam.getSlashing().getSlashBlocksReward();
        //默认每个区块的最大Gas
        this.maxBlockGasLimit=modifiableParam.getBlock().getMaxBlockGasLimit();
    }
}