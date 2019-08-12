package com.platon.browser.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:12
 * @Description:
 */
@Component
public class BlockChainConfig {
    // 增发周期
    @Value("platonconfig.add.issue.period")
    private String addIssuePeriod;

    // 结算周期
    @Value("platonconfig.setting.period")
    private String settingPeriod;

    // 共识周期
    @Value("platonconfig.consensus.period")
    private String consensusPeriod;

    // 增发率
    @Value("platonconfig.add.issue.rate")
    private String addIssueRate;

    // 出块激励比例
    @Value("platonconfig.block.reward.rate")
    private String blockRewardRate;

    // 质押激励比例
    @Value("platonconfig.staking.reward.rate")
    private String stakingRewardRate;

    // 初始发行金额
    @Value("platonconfig.init.issue.value")
    private String initIssueValue;

    // 激励合约地址
    @Value("platonconfig.reward.addr")
    private String rewardAddr;

    // 质押合约地址
    @Value("platonconfig.staking.addr")
    private String stakingAddr;

    // 锁仓合约地址
    @Value("platonconfig.restricting.addr")
    private String restrictingAddr;

    // 质押门槛
    @Value("platonconfig.stake.threshold")
    private String stakeThreshold;

    // 委托门槛
    @Value("platonconfig.minimum.threshold")
    private String minimumThreshold;

    // 每个共识轮中选举下一轮验证人的块高间隔(由后往前计算)
    @Value("platonconfig.election.distance")
    private String electionDistance;

    // 投票数百分比下限
    @Value("platonconfig.support.rate.threshold")
    private String supportRateThreshold;

    // 双签低处罚金额，百分比
    @Value("platonconfig.duplicate.sign.low.slashing")
    private String duplicateSignLowSlashing;

    // 每个共识轮低异常的出块数
    @Value("platonconfig.ack.amount.abnormal")
    private String ackAmountAbnormal;

    // 每个共识轮高异常的出块数
    @Value("platonconfig.pack.amount.high.abnormal")
    private String packAmountHighAbnormal;

    // 每个共识轮低异常出块数的处罚额度，百分比
    @Value("platonconfig.pack.amount.low.slash.rate")
    private String packAmountLowSlashRate;

    // 每个共识轮高异常出块数的处罚额度，百分比
    @Value("platonconfig.pack.amount.high.slash.rate")
    private String packAmountHighSlashRate;

    public String getAddIssuePeriod () {
        return addIssuePeriod;
    }

    public String getSettingPeriod () {
        return settingPeriod;
    }

    public String getConsensusPeriod () {
        return consensusPeriod;
    }

    public String getAddIssueRate () {
        return addIssueRate;
    }

    public String getBlockRewardRate () {
        return blockRewardRate;
    }

    public String getStakingRewardRate () {
        return stakingRewardRate;
    }

    public String getInitIssueValue () {
        return initIssueValue;
    }

    public String getRewardAddr () {
        return rewardAddr;
    }

    public String getStakingAddr () {
        return stakingAddr;
    }

    public String getRestrictingAddr () {
        return restrictingAddr;
    }

    public String getStakeThreshold () {
        return stakeThreshold;
    }

    public String getMinimumThreshold () {
        return minimumThreshold;
    }

    public String getElectionDistance () {
        return electionDistance;
    }

    public String getSupportRateThreshold () {
        return supportRateThreshold;
    }

    public String getDuplicateSignLowSlashing () {
        return duplicateSignLowSlashing;
    }

    public String getAckAmountAbnormal () {
        return ackAmountAbnormal;
    }

    public String getPackAmountHighAbnormal () {
        return packAmountHighAbnormal;
    }

    public String getPackAmountLowSlashRate () {
        return packAmountLowSlashRate;
    }

    public String getPackAmountHighSlashRate () {
        return packAmountHighSlashRate;
    }
}
