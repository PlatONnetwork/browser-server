package com.platon.browser.config;

import com.platon.browser.enums.InnerContractAddrEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix="platon.config")
public class BlockChainConfig {
    public static final Set<String> INNER_CONTRACT_ADDR = new HashSet<>(InnerContractAddrEnum.addresses);

    @PostConstruct
    private void init(){
        INNER_CONTRACT_ADDR.add(developerIncentiveFundAccountAddr);
        INNER_CONTRACT_ADDR.add(platonFundAccountAddr);
    }

    // 初始验证人锁定质押金(LAT)
    private BigDecimal initValidatorStakingLockedAmount;
    // 质押节点统计年化率最多取多少个连续周期
    private BigInteger maxSettlePeriodCount4AnnualizedRateStat;
    // 链ID
    private String chainId;
    // PlatON初始总发行量
    private BigDecimal initIssueAmount;
    // 每年固定增发比例
    private BigDecimal addIssueRate;
    // 每年增发分配给激励池的比例
    private BigDecimal incentiveRateFromIssue;
    // 激励池分配给出块激励的比例
    private BigDecimal blockRewardRate;
    //激励池分配给质押激励的比例
    private BigDecimal stakeRewardRate;
    //触发普通处罚的出块率，例如以出块数量目标值10为标准，如果实际出块数<=10*0.6，则执行普通处罚
    private BigDecimal blockRate4LowSlash;
    // 普通质押金处罚百分比
    private BigDecimal blockLowSlashRate;
    //触发最高处罚的出块率，例如以出块数量目标值10为标准，如果实际出块数<=10*0.2，则执行最高处罚
    private BigDecimal blockRate4HighSlash;
    //违规-低出块率: 最高处罚百分比
    private BigDecimal blockHighSlashRate;
    //质押门槛: 创建验证人最低的质押Token数
    private BigDecimal stakeThreshold;
    //委托门槛
    private BigDecimal delegateThreshold;
    //出块间隔(秒)
    private int blockInterval;
    //每个验证人每个共识周期出块数量目标值
    private BigInteger expectBlockCount;
    //共识轮区块数
    private BigInteger consensusPeriodBlockCount;
    //每个共识轮中回退多少个块是选举下一轮验证人的时机
    private BigInteger electionBackwardBlockCount;
    //每个结算周期区块总数(等于43个共识周期出的块数总和)
    private BigInteger settlePeriodBlockCount;
    //每个增发周期区块总数(1466个结算周期=15759500个块)
    private BigInteger addIssuePeriodBlockCount;
    //节点质押退回锁定的结算周期数
    private BigInteger unstakeRefundSettlePeriodCount;
    //文本提案参与率: >
    private BigDecimal minProposalTextParticipationRate;
    //文本提案支持率：>=
    private BigDecimal minProposalTextSupportRate;
    //取消提案参与率: >
    private BigDecimal minProposalCancelParticipationRate;
    //取消提案支持率：>=
    private BigDecimal minProposalCancelSupportRate;
    //升级提案通过率
    private BigDecimal minProposalUpgradePassRate;
    //双签低处罚百分比
    private BigDecimal duplicateSignLowSlashRate;
    //开发者激励基金账户地址
    private String developerIncentiveFundAccountAddr;
    //PlatOn基金会账户地址
    private String platonFundAccountAddr;
    //10年内基金会向激励池填充额度
    private Map<Integer,BigDecimal> foundationSubsidies;
    //提案url参数模板
    private String proposalUrlTemplate;
}
