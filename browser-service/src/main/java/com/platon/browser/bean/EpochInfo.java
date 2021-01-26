package com.platon.browser.bean;

import com.platon.utils.Numeric;

import java.math.BigDecimal;

public class EpochInfo {
    private BigDecimal packageReward; // 出块奖励
    private BigDecimal stakingReward; // 结算周期质押奖励
    private BigDecimal yearNum; // 当前增发周期
    private BigDecimal yearStartNum; // 当前增发周期开始区块号
    private BigDecimal yearEndNum; // 当前增发周期结束区块号
    private BigDecimal remainEpoch; // 当前增发周期剩下的结算周期数
    private BigDecimal avgPackTime; // 平均出块时间

    public BigDecimal getPackageReward() {
        return packageReward;
    }

    public void setPackageReward(String packageReward) {
        this.packageReward = new BigDecimal(Numeric.decodeQuantity(packageReward));
    }

    public BigDecimal getStakingReward() {
        return stakingReward;
    }

    public void setStakingReward(String stakingReward) {
        this.stakingReward = new BigDecimal(Numeric.decodeQuantity(stakingReward));
    }

    public BigDecimal getYearNum() {
        return yearNum;
    }

    public void setYearNum(BigDecimal yearNum) {
        this.yearNum = yearNum;
    }

    public BigDecimal getYearStartNum() {
        return yearStartNum;
    }

    public void setYearStartNum(BigDecimal yearStartNum) {
        this.yearStartNum = yearStartNum;
    }

    public BigDecimal getYearEndNum() {
        return yearEndNum;
    }

    public void setYearEndNum(BigDecimal yearEndNum) {
        this.yearEndNum = yearEndNum;
    }

    public BigDecimal getRemainEpoch() {
        return remainEpoch;
    }

    public void setRemainEpoch(BigDecimal remainEpoch) {
        this.remainEpoch = remainEpoch;
    }

    public BigDecimal getAvgPackTime() {
        return avgPackTime;
    }

    public void setAvgPackTime(BigDecimal avgPackTime) {
        this.avgPackTime = avgPackTime;
    }
}