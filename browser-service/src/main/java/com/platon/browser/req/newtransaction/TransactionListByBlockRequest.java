package com.platon.browser.req.newtransaction;

import com.platon.browser.req.PageReq;

import lombok.Data;

/**
 * 区块交易请求对象
 *  @file TransactionListByBlockRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class TransactionListByBlockRequest extends PageReq{
    private Integer blockNumber;
    private String txType;
}