package com.platon.browser.req.newtransaction;

import com.platon.browser.req.PageReq;

import lombok.Data;

@Data
public class TransactionListByBlockRequest extends PageReq{
    private Integer blockNumber;
    private String txType;
}