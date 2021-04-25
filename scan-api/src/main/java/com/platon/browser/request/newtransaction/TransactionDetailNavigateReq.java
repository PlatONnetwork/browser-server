package com.platon.browser.request.newtransaction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 *  交易详情请求对象
 *  @file TransactionDetailNavigateReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailNavigateReq {
    @NotBlank(message = "{transaction.hash.notnull}")
    private String txHash;
    @NotBlank(message = "{navigate.direction.notnull}")
    @Pattern(regexp = "prev|next", message = "{direction.illegal}")
    private String direction;
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
}
