package com.platon.browser.res.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *  交易详情锁仓子结构体返回对象
 *  @file TransactionDetailsRPPlanResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsRPPlanResp {
	private BigInteger epoch;         //锁仓周期
    private BigDecimal amount;      //锁定金额
    private Long blockNumber;   //锁仓周期对应快高  结束周期 * epoch  
	public BigInteger getEpoch() {
		return epoch;
	}
	public void setEpoch(BigInteger epoch) {
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
