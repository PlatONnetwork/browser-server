package com.platon.browser.client;

import org.web3j.utils.Numeric;

import java.math.BigDecimal;

public class EpochInfo {
    private BigDecimal PackageReward; // 出块奖励
    private BigDecimal StakingReward; // 质押奖励
    private int YearNum; // 当前增发周期
    private int RemainBlocks; // 当前增发周期剩下的区块数
    private int RemainEpoch; // 当前增发周期剩下的结算周期数
    private int AvgPackTime; // 平均出块时间

    public BigDecimal getPackageReward() {
        return PackageReward;
    }

    public void setPackageReward(String packageReward) {
        PackageReward = new BigDecimal(Numeric.decodeQuantity(packageReward));
    }

    public BigDecimal getStakingReward() {
        return StakingReward;
    }

    public void setStakingReward(String stakingReward) {
        StakingReward = new BigDecimal(Numeric.decodeQuantity(stakingReward));
    }

    public int getYearNum() {
        return YearNum;
    }

    public void setYearNum(int yearNum) {
        YearNum = yearNum;
    }

    public int getRemainBlocks() {
        return RemainBlocks;
    }

    public void setRemainBlocks(int remainBlocks) {
        RemainBlocks = remainBlocks;
    }

    public int getRemainEpoch() {
        return RemainEpoch;
    }

    public void setRemainEpoch(int remainEpoch) {
        RemainEpoch = remainEpoch;
    }

    public int getAvgPackTime() {
        return AvgPackTime;
    }

    public void setAvgPackTime(int avgPackTime) {
        AvgPackTime = avgPackTime;
    }
}