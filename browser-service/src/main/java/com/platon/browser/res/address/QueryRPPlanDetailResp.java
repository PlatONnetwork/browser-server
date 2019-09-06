package com.platon.browser.res.address;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 *  查询地址锁仓信息的返回的对象
 *  @file QueryRPPlanDetailResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class QueryRPPlanDetailResp {
	private String restrictingBalance;           //锁仓余额(单位:LAT)
	private String stakingValue;  //锁仓质押\委托(单位:LAT)
	private String underreleaseValue;        //欠释放(单位:LAT)
	private List<DetailsRPPlanResp> RPPlan;  //锁仓计划
	private Long total;
	private String totalValue;//总计锁仓
	
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getRestrictingBalance() {
		return restrictingBalance;
	}
	public void setRestrictingBalance(String restrictingBalance) {
		this.restrictingBalance = restrictingBalance;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getStakingValue() {
		return stakingValue;
	}
	public void setStakingValue(String stakingValue) {
		this.stakingValue = stakingValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getUnderreleaseValue() {
		return underreleaseValue;
	}
	public void setUnderreleaseValue(String underreleaseValue) {
		this.underreleaseValue = underreleaseValue;
	}
	public List<DetailsRPPlanResp> getRPPlan() {
		return RPPlan;
	}
	public void setRPPlan(List<DetailsRPPlanResp> rPPlan) {
		RPPlan = rPPlan;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}
	
}
