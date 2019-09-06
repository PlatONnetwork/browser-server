package com.platon.browser.res.address;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 *  地址详情锁仓子结构体返回对象
 *  @file DetailsRPPlanResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class DetailsRPPlanResp {
	private Long epoch;         //锁仓周期
    private String amount;      //锁定金额
    private Long blockNumber;   //锁仓周期对应快高  结束周期 * epoch  
    private Long estimateTime;   //预计时间
	public Long getEpoch() {
		return epoch;
	}
	public void setEpoch(Long epoch) {
		this.epoch = epoch;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Long getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}
	public Long getEstimateTime() {
		return estimateTime;
	}
	public void setEstimateTime(Long estimateTime) {
		this.estimateTime = estimateTime;
	}
	
    
}
