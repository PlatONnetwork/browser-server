package com.platon.browser.engine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix="platon.config")
public class BlockChainConfig {
    // 链ID
    private String chainId;
    // 增发周期
    private BigInteger addIssuePeriod;
    // 结算周期
    private BigInteger settingPeriod;
    // 共识周期
    private BigInteger consensusPeriod;
    // 增发率
    private BigDecimal addIssueRate;
    // 出块激励比例
    private BigDecimal blockRewardRate;
    // 质押激励比例
    private BigDecimal stakingRewardRate;
    // 初始发行金额
    private BigInteger initIssueValue;

    // 锁仓账户地址
    private String restrictingAccountAddr;
    // 激励池账户地址
    private String stimulatePoolAccountAddr;
    // 开发者激励基金账户地址
    private String developerStimulateFundAccountAddr;
    // PlatOn基金会账户地址
    private String platonFundAccountAddr;

    // 质押门槛
    private BigDecimal stakeThreshold;
    // 委托门槛
    private BigDecimal minimumThreshold;
    // 每个共识轮中选举下一轮验证人的块高间隔(由后往前计算)
    private BigInteger electionDistance;
    // 投票数百分比下限
    private BigDecimal supportRateThreshold;
    // 双签低处罚金额，百分比
    private BigDecimal duplicateSignLowSlashing;
    // 每个共识轮低异常的出块数
    private BigInteger packAmountAbnormal;
    // 每个共识轮高异常的出块数
    private BigInteger packAmountHighAbnormal;
    // 每个共识轮低异常出块数的处罚额度，百分比
    private BigDecimal packAmountLowSlashRate;
    // 每个共识轮高异常出块数的处罚额度，百分比
    private BigDecimal packAmountHighSlashRate;

    private Map <String,String> foundation;
}
