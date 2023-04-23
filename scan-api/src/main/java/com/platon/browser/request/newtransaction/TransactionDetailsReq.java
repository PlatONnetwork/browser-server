package com.platon.browser.request.newtransaction;

import com.platon.browser.utils.HexUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 交易详情请求对象
 *  @file TransactionDetailsReq.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsReq{
    @NotBlank(message = "{txHash not null}")
    @Size(min = 60,max = 66)
    private String txHash;

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		if(StringUtils.isBlank(txHash)) return;
		this.txHash = HexUtil.prefix(txHash.toLowerCase());
	}

}
