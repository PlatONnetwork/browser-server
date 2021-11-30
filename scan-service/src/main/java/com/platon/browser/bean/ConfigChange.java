package com.platon.browser.bean;

import java.math.BigDecimal;

public class ConfigChange {
    private BigDecimal blockReward; // 出块奖励
    private BigDecimal settleStakeReward; // 结算周期质押奖励
    private BigDecimal issueEpoch ; // 当前增发周期
    private BigDecimal yearStartNum; // 当前增发周期开始区块号
    private BigDecimal yearEndNum; // 当前增发周期结束区块号
    private BigDecimal remainEpoch; // 当前增发周期剩下的结算周期数
    private BigDecimal avgPackTime; // 平均出块时间
    private BigDecimal stakeReward; // 当前结算周期内每个验证人的质押奖励
    private String issueRates; // 当前增发比例

    public BigDecimal getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(BigDecimal blockReward) {
        this.blockReward = blockReward;
    }

    public BigDecimal getSettleStakeReward() {
        return settleStakeReward;
    }

    public void setSettleStakeReward(BigDecimal settleStakeReward) {
        this.settleStakeReward = settleStakeReward;
    }

    public BigDecimal getIssueEpoch() {
        return issueEpoch;
    }

    public void setIssueEpoch(BigDecimal issueEpoch) {
        this.issueEpoch = issueEpoch;
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

    public BigDecimal getStakeReward() {
        return stakeReward;
    }

    public void setStakeReward(BigDecimal stakeReward) {
        this.stakeReward = stakeReward;
    }

	public String getIssueRates() {
		return issueRates;
	}

	public void setIssueRates(String issueRates) {
		this.issueRates = issueRates;
	}
}
