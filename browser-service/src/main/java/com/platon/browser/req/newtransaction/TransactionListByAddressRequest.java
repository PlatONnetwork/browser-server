package com.platon.browser.req.newtransaction;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

/**
 * 地址交易列表请求对象
 *  @file TransactionListByAddressRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionListByAddressRequest extends PageReq{
	@NotBlank(message = "{address not null}")
    private String address;
    private String txType;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address.toLowerCase();
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
    
}