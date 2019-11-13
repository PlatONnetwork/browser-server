package com.platon.browser.res.transaction;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 *  交易详情锁仓子结构体返回对象
 *  @file TransactionDetailsRPPlanResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsRPPlanResp {
	private Integer epoch;         //锁仓周期
    private BigDecimal amount;      //锁定金额
    private Long blockNumber;   //锁仓周期对应快高  结束周期 * epoch  
	public Integer getEpoch() {
		return epoch;
	}
	public void setEpoch(Integer epoch) {
		this.epoch = epoch;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}
    
}
