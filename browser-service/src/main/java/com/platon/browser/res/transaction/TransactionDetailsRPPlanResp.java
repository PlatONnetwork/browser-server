package com.platon.browser.res.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class TransactionDetailsRPPlanResp {
	private Integer epoch;         //锁仓周期
    private String amount;      //锁定金额
    private Long blockNumber;   //锁仓周期对应快高  结束周期 * epoch  
	public Integer getEpoch() {
		return epoch;
	}
	public void setEpoch(Integer epoch) {
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
    
}
