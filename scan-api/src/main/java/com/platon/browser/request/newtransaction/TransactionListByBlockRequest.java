package com.platon.browser.request.newtransaction;

import com.platon.browser.request.PageReq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 区块交易请求对象
 *  @file TransactionListByBlockRequest.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionListByBlockRequest extends PageReq{
	@NotNull(message = "{blockNumber not null}")
	@Min(value = 0)
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
