package com.platon.browser.req.newtransaction;

import com.platon.browser.req.PageReq;

import lombok.Data;

/**
 * 地址交易列表请求对象
 *  @file TransactionListByAddressRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class TransactionListByAddressRequest extends PageReq{
    private String address;
    private String txType;
}