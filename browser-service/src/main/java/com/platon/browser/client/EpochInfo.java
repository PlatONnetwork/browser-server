package com.platon.browser.client;

import org.web3j.utils.Numeric;

import java.math.BigDecimal;

public class EpochInfo {
    private BigDecimal PackageReward; // 出块奖励
    private BigDecimal StakingReward; // 结算周期质押奖励
    private BigDecimal YearNum; // 当前增发周期
    private BigDecimal YearStartNum; // 当前增发周期开始区块号
    private BigDecimal YearEndNum; // 当前增发周期结束区块号
    private BigDecimal RemainEpoch; // 当前增发周期剩下的结算周期数
    private BigDecimal AvgPackTime; // 平均出块时间

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

    public BigDecimal getYearNum() {
        return YearNum;
    }

    public void setYearNum(BigDecimal yearNum) {
        YearNum = yearNum;
    }

    public BigDecimal getYearStartNum() {
        return YearStartNum;
    }

    public void setYearStartNum(BigDecimal yearStartNum) {
        YearStartNum = yearStartNum;
    }

    public BigDecimal getYearEndNum() {
        return YearEndNum;
    }

    public void setYearEndNum(BigDecimal yearEndNum) {
        YearEndNum = yearEndNum;
    }

    public BigDecimal getRemainEpoch() {
        return RemainEpoch;
    }

    public void setRemainEpoch(BigDecimal remainEpoch) {
        RemainEpoch = remainEpoch;
    }

    public BigDecimal getAvgPackTime() {
        return AvgPackTime;
    }

    public void setAvgPackTime(BigDecimal avgPackTime) {
        AvgPackTime = avgPackTime;
    }
}