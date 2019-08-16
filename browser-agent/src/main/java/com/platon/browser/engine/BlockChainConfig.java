package com.platon.browser.engine;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
    private Long addIssuePeriod;
    // 结算周期
    private Long settingPeriod;
    // 共识周期
    private Long consensusPeriod;
    // 增发率
    private Double addIssueRate;
    // 出块激励比例
    private Double blockRewardRate;
    // 质押激励比例
    private Double stakingRewardRate;
    // 初始发行金额
    private String initIssueValue;
    // 激励合约地址
    private String rewardAddr;
    // 质押合约地址
    private String stakingAddr;
    // 锁仓合约地址
    private String restrictingAddr;
    // 质押门槛
    private String stakeThreshold;
    // 委托门槛
    private String minimumThreshold;
    // 每个共识轮中选举下一轮验证人的块高间隔(由后往前计算)
    private Long electionDistance;
    // 投票数百分比下限
    private String supportRateThreshold;
    // 双签低处罚金额，百分比
    private Double duplicateSignLowSlashing;
    // 每个共识轮低异常的出块数
    private String packAmountAbnormal;
    // 每个共识轮高异常的出块数
    private String packAmountHighAbnormal;
    // 每个共识轮低异常出块数的处罚额度，百分比
    private Double packAmountLowSlashRate;
    // 每个共识轮高异常出块数的处罚额度，百分比
    private Double packAmountHighSlashRate;
}
