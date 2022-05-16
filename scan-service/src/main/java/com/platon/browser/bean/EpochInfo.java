package com.platon.browser.bean;

import com.platon.utils.Numeric;

import java.math.BigDecimal;

/**
 * 结算周期信息
 *
 * @date: 2021/12/22
 */
public class EpochInfo {

    /**
     * 出块奖励--废弃
     */
    private BigDecimal packageReward;

    /**
     * 结算周期质押奖励--废弃
     */
    private BigDecimal stakingReward;

    /**
     * 当前增发周期
     */
    private BigDecimal yearNum;

    /**
     * 当前增发周期开始区块号
     */
    private BigDecimal yearStartNum;

    /**
     * 当前增发周期结束区块号
     */
    private BigDecimal yearEndNum;

    /**
     * 当前增发周期剩下的结算周期数
     */
    private BigDecimal remainEpoch;

    /**
     * 平均出块时间
     */
    private BigDecimal avgPackTime;

    /**
     * 当前结算周期的出块奖励
     */
    private BigDecimal curPackageReward;

    /**
     * 当前结算周期的质押奖励
     */
    private BigDecimal curStakingReward;

    /**
     * 下一个结算周期的出块奖励
     */
    private BigDecimal nextPackageReward;

    /**
     * 下一个结算周期的质押奖励
     */
    private BigDecimal nextStakingReward;

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

    public BigDecimal getCurPackageReward() {
        return curPackageReward;
    }

    public void setCurPackageReward(String curPackageReward) {
        this.curPackageReward = new BigDecimal(Numeric.decodeQuantity(curPackageReward));
    }

    public BigDecimal getCurStakingReward() {
        return curStakingReward;
    }

    public void setCurStakingReward(String curStakingReward) {
        this.curStakingReward = new BigDecimal(Numeric.decodeQuantity(curStakingReward));
    }

    public BigDecimal getNextPackageReward() {
        return nextPackageReward;
    }

    public void setNextPackageReward(String nextPackageReward) {
        this.nextPackageReward = new BigDecimal(Numeric.decodeQuantity(nextPackageReward));
    }

    public BigDecimal getNextStakingReward() {
        return nextStakingReward;
    }

    public void setNextStakingReward(String nextStakingReward) {
        this.nextStakingReward = new BigDecimal(Numeric.decodeQuantity(nextStakingReward));
    }

}