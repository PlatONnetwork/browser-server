package com.platon.browser.req.newtransaction;

import javax.validation.constraints.NotBlank;

import com.platon.browser.utils.HexTool;

/**
 * 交易详情请求对象
 *  @file TransactionDetailsReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsReq{
    @NotBlank(message = "{txHash not null}")
    private String txHash;

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = HexTool.prefix(txHash.toLowerCase());
	}
    
}