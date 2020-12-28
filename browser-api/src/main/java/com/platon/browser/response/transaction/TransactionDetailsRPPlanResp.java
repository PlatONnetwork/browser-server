package com.platon.browser.response.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 *  交易详情锁仓子结构体返回对象
 *  @file TransactionDetailsRPPlanResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsRPPlanResp {
	private String epoch;         //锁仓周期
    private BigDecimal amount;      //锁定金额
    private String blockNumber;   //锁仓周期对应快高  结束周期 * epoch  
	public String getEpoch() {
		return epoch;
	}
	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}
    
}
