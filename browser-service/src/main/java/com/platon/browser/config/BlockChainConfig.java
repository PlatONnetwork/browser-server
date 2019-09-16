package com.platon.browser.config;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.bean.EconomicConfigParam;
import com.platon.browser.config.bean.EconomicConfigResult;
import com.platon.browser.config.bean.Web3Response;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.HttpRequestException;
import com.platon.browser.util.HttpUtil;
import com.platon.browser.util.LocalSettingsEnvironmentPostProcessor;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
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
import java.util.concurrent.TimeUnit;

/**
 * 链参数统一配置项
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix="platon.config")
public class BlockChainConfig {
    private static Logger logger = LoggerFactory.getLogger(BlockChainConfig.class);

    static {
        File saltFile = FileUtils.getFile(System.getProperty("user.dir"), "jasypt.properties");
        Properties properties = new Properties();
        try(InputStream in = new FileInputStream(saltFile);) {
            properties.load(in);
            String salt=properties.getProperty("jasypt.encryptor.password");
            if(StringUtils.isBlank(salt)) throw new RuntimeException("加密盐不能为空!");
            salt=salt.trim();
            System.setProperty("JASYPT_ENCRYPTOR_PASSWORD",salt);
            logger.error("salt:{}",salt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private PlatonClient client;

    public static final Set<String> INNER_CONTRACT_ADDR = new HashSet<>(InnerContractAddrEnum.ADDRESSES);

    /*******************以下参数通过rpc接口debug_economicConfig获取*******************/
    //【通用】每个验证人每个共识周期出块数量目标值
    private BigInteger expectBlockCount;
    //【通用】每个共识轮验证节点数量
    private BigInteger consensusValidatorCount;
    //【通用】每个增发周期的分钟数
    private BigInteger additionalCycleMinutes;
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

    //【质押】质押门槛: 创建验证人最低的质押Token数(LAT)
    private BigDecimal stakeThreshold;
    //【质押】委托门槛(LAT)
    private BigDecimal delegateThreshold;
    //【质押】节点质押退回锁定的结算周期数
    private BigInteger unStakeRefundSettlePeriodCount;

    //【惩罚】违规-低出块率-触发处罚的出块率阈值 60%
    private BigDecimal slashBlockRate;
    //【惩罚】低出块率处罚多少个区块奖励
    private BigDecimal slashBlockCount;
    //【惩罚】双签处罚百分比
    private BigDecimal duplicateSignSlashRate;

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

    //【奖励】激励池分配给出块激励的比例
    private BigDecimal blockRewardRate;
    //【奖励】激励池分配给质押激励的比例
    private BigDecimal stakeRewardRate;

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
    //开发者激励基金账户地址
    private String developerIncentiveFundAccountAddr;
    //PlatOn基金会账户地址
    private String platonFundAccountAddr;
    //10年内基金会向激励池填充额度(LAT)
    private Map<Integer,BigDecimal> foundationSubsidies;
    //提案url参数模板
    private String proposalUrlTemplate;
    //keyBase
    private String keyBase;
    // 初始内置节点默认质押金额(LAT)
    private BigDecimal defaultStakingLockedAmount;
    // 初始内置节点信息
    private List<CustomStaking> defaultStakings=new ArrayList<>();

    @PostConstruct
    private void init() throws InterruptedException {
        EconomicConfigParam ecp = new EconomicConfigParam("2.0","debug_economicConfig",Collections.emptyList(),1);
        String param = JSON.toJSONString(ecp);
        EconomicConfigResult ecr;
        String web3jAddress=null;
        while (true) try {
            web3jAddress = client.getWeb3jAddress();
            logger.info("Web3j RPC:{}",web3jAddress);
            Web3Response response = HttpUtil.post(web3jAddress, param, Web3Response.class);
            ecr = JSON.parseObject(response.getResult(), EconomicConfigResult.class);
            break;
        } catch (HttpRequestException e) {
            logger.error("初始化链配置错误,将重试:we3j={},error={}", web3jAddress, e.getMessage());
            TimeUnit.SECONDS.sleep(1);
        }
        logger.info("链上配置:{}",JSON.toJSONString(ecr,true));
        updateWithEconomicConfigResult(ecr);
    }

    private void updateWithEconomicConfigResult(EconomicConfigResult ecr) {
        //【通用】每个验证人每个共识周期出块数量目标值
        this.expectBlockCount=ecr.getCommon().getPerRoundBlocks();
        //【通用】每个共识轮验证节点数量
        this.consensusValidatorCount=ecr.getCommon().getValidatorCount();
        //【通用】增发周期规定的分钟数
        this.additionalCycleMinutes=ecr.getCommon().getAdditionalCycleTime();
        //【通用】出块间隔 = 系统分配的节点出块时间窗口/每个验证人每个view出块数量目标值
        this.blockInterval=ecr.getCommon().getNodeBlockTimeWindow().divide(this.expectBlockCount);
        //【通用】共识轮区块数 = expectBlockCount x consensusValidatorCount
        this.consensusPeriodBlockCount=this.expectBlockCount.multiply(ecr.getCommon().getValidatorCount());
        //【通用】每个结算周期区块总数=ROUND_DOWN(结算周期规定的分钟数x60/(出块间隔x共识轮区块数))x共识轮区块数
        this.settlePeriodBlockCount=ecr.getCommon().getExpectedMinutes()
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
        //【质押】创建验证人最低的质押Token数(LAT)
        this.stakeThreshold= Convert.fromVon(ecr.getStaking().getStakeThreshold(), Convert.Unit.LAT);
        //【质押】委托人每次委托及赎回的最低Token数(LAT)
        this.delegateThreshold=Convert.fromVon(ecr.getStaking().getMinimumThreshold(), Convert.Unit.LAT);
        //【质押】节点质押退回锁定的结算周期数
        this.unStakeRefundSettlePeriodCount=ecr.getStaking().getUnStakeFreezeRatio();

        //【惩罚】违规-低出块率-触发处罚的出块率阈值 60%
        this.slashBlockRate=ecr.getSlashing().getPackAmountAbnormal().divide(BigDecimal.valueOf(10),2, RoundingMode.FLOOR);
        //【惩罚】低出块率处罚多少个区块奖励
        this.slashBlockCount=ecr.getSlashing().getNumberOfBlockRewardForSlashing();
        //【惩罚】双签处罚百分比
        this.duplicateSignSlashRate=ecr.getSlashing().getDuplicateSignHighSlashing();
        //【惩罚】双签处罚百分比
        this.duplicateSignSlashRate=ecr.getSlashing().getDuplicateSignHighSlashing().divide(BigDecimal.valueOf(100),2, RoundingMode.FLOOR);
        //【治理】文本提案参与率: >
        this.minProposalTextParticipationRate=ecr.getGov().getTextProposal_VoteRate();
        //【治理】文本提案支持率：>=
        this.minProposalTextSupportRate=ecr.getGov().getTextProposal_SupportRate();
        //【治理】取消提案参与率: >
        this.minProposalCancelParticipationRate=ecr.getGov().getCancelProposal_VoteRate();
        //【治理】取消提案支持率：>=
        this.minProposalCancelSupportRate=ecr.getGov().getCancelProposal_SupportRate();
        //【治理】升级提案通过率
        this.minProposalUpgradePassRate=ecr.getGov().getVersionProposal_SupportRate();
        //【治理】文本提案投票周期
        this.proposalTextConsensusRounds=ecr.getGov().getTextProposalVote_DurationSeconds()
                .divide(new BigDecimal(this.blockInterval.multiply(ecr.getCommon().getPerRoundBlocks()).multiply(ecr.getCommon().getValidatorCount())),0,RoundingMode.FLOOR);
        //【治理】设置预升级开始轮数
        this.versionProposalActiveConsensusRounds=ecr.getGov().getVersionProposalVote_DurationSeconds()
                .divide(new BigDecimal(this.blockInterval.multiply(ecr.getCommon().getPerRoundBlocks()).multiply(ecr.getCommon().getValidatorCount())),0,RoundingMode.FLOOR);
        //【奖励】激励池分配给出块激励的比例
        this.blockRewardRate=ecr.getReward().getNewBlockRate().divide(BigDecimal.valueOf(100),2,RoundingMode.FLOOR);
        //【奖励】激励池分配给质押激励的比例 = 1-区块奖励比例
        this.stakeRewardRate=BigDecimal.ONE.subtract(this.blockRewardRate);

    }
}
