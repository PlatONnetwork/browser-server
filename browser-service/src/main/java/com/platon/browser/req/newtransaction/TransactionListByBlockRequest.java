package com.platon.browser.req.newtransaction;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

/**
 * 区块交易请求对象
 *  @file TransactionListByBlockRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionListByBlockRequest extends PageReq{
	@NotBlank(message = "{blockNumber not null}")
    private Integer blockNumber;
    private String txType;
	public Integer getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Integer blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
    
}