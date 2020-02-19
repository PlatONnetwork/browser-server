package com.platon.browser.res.staking;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 * 验证人统计参数返回对象
 *  @file StakingStatisticNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class StakingStatisticNewResp {

	private BigDecimal stakingDelegationValue; // 质押委托总数
	private BigDecimal stakingValue; // 质押总数
	private BigDecimal delegationValue; // 质押委托总数
	private BigDecimal issueValue; // 发行量
	private BigDecimal blockReward; // 当前的出块奖励
	private BigDecimal stakingReward; // 当前的质押奖励
	private Long currentNumber; // 当前区块高度
	private Long addIssueBegin; // 当前增发周期的开始快高
	private Long addIssueEnd; // 当前增发周期的结束块高
	private Long nextSetting; // 离下个结算周期倒计时
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getStakingDelegationValue() {
		return stakingDelegationValue;
	}
	public void setStakingDelegationValue(BigDecimal stakingDelegationValue) {
		this.stakingDelegationValue = stakingDelegationValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getStakingValue() {
		return stakingValue;
	}
	public void setStakingValue(BigDecimal stakingValue) {
		this.stakingValue = stakingValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getIssueValue() {
		return issueValue;
	}
	public void setIssueValue(BigDecimal issueValue) {
		this.issueValue = issueValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getBlockReward() {
		return blockReward;
	}
	public void setBlockReward(BigDecimal blockReward) {
		this.blockReward = blockReward;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getStakingReward() {
		return stakingReward;
	}
	public void setStakingReward(BigDecimal stakingReward) {
		this.stakingReward = stakingReward;
	}
	public Long getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(Long currentNumber) {
		this.currentNumber = currentNumber;
	}
	public Long getAddIssueBegin() {
		return addIssueBegin;
	}
	public void setAddIssueBegin(Long addIssueBegin) {
		this.addIssueBegin = addIssueBegin;
	}
	public Long getAddIssueEnd() {
		return addIssueEnd;
	}
	public void setAddIssueEnd(Long addIssueEnd) {
		this.addIssueEnd = addIssueEnd;
	}
	public Long getNextSetting() {
		return nextSetting;
	}
	public void setNextSetting(Long nextSetting) {
		this.nextSetting = nextSetting;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getDelegationValue() {
		return delegationValue;
	}
	public void setDelegationValue(BigDecimal delegationValue) {
		this.delegationValue = delegationValue;
	}
	
}
