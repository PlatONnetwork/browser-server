package com.platon.browser.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.math.BigInteger;

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
    private BigDecimal chainAge;

    /**
     * 当前增发周期开始区块号
     */
    private BigDecimal yearStartBlockNum;

    /**
     * 当前增发周期结束区块号
     */
    private BigDecimal yearEndBlockNum;

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

    public void setPackageReward(BigInteger packageReward) {
        this.packageReward = new BigDecimal(null == packageReward ? new BigInteger("0"):packageReward);
    }

    public BigDecimal getStakingReward() {
        return stakingReward;
    }

    public void setStakingReward(BigInteger stakingReward) {
        this.stakingReward = new BigDecimal(null == stakingReward ? new BigInteger("0"):stakingReward);
    }

    public BigDecimal getChainAge() {
        return chainAge;
    }

    public void setChainAge(BigInteger chainAge) {
        this.chainAge = new BigDecimal(null == chainAge ? new BigInteger("0"): chainAge);
    }

    public BigDecimal getYearStartBlockNum() {
        return yearStartBlockNum;
    }

    public void setYearStartBlockNum(BigInteger yearStartBlockNum) {
        this.yearStartBlockNum = new BigDecimal(null == yearStartBlockNum ? new BigInteger("0"): yearStartBlockNum);
    }

    public BigDecimal getYearEndBlockNum() {
        return yearEndBlockNum;
    }

    public void setYearEndBlockNum(BigInteger yearEndBlockNum) {
        this.yearEndBlockNum = new BigDecimal(null == yearEndBlockNum ? new BigInteger("0"): yearEndBlockNum);

    }

    public BigDecimal getRemainEpoch() {
        return remainEpoch;
    }

    public void setRemainEpoch(BigInteger remainEpoch) {
        this.remainEpoch = new BigDecimal(null == remainEpoch ? new BigInteger("0"):remainEpoch);

    }

    public BigDecimal getAvgPackTime() {
        return avgPackTime;
    }

    public void setAvgPackTime(BigInteger avgPackTime) {
        this.avgPackTime = new BigDecimal(null == avgPackTime ? new BigInteger("0"):avgPackTime);

    }

    public BigDecimal getCurPackageReward() {
        return curPackageReward;
    }

    public void setCurPackageReward(BigInteger curPackageReward) {
        this.curPackageReward = new BigDecimal(null == curPackageReward ? new BigInteger("0"):curPackageReward);
    }

    public BigDecimal getCurStakingReward() {
        return curStakingReward;
    }

    public void setCurStakingReward(BigInteger curStakingReward) {
        this.curStakingReward = new BigDecimal(null == curStakingReward ? new BigInteger("0"):curStakingReward);
    }

    public BigDecimal getNextPackageReward() {
        return nextPackageReward;
    }

    public void setNextPackageReward(BigInteger nextPackageReward) {
        this.nextPackageReward = new BigDecimal(null == nextPackageReward ? new BigInteger("0"):nextPackageReward);
    }

    public BigDecimal getNextStakingReward() {
        return nextStakingReward;
    }

    public void setNextStakingReward(BigInteger nextStakingReward) {
        this.nextStakingReward = new BigDecimal(null == nextStakingReward ? new BigInteger("0"):nextStakingReward);
    }

}