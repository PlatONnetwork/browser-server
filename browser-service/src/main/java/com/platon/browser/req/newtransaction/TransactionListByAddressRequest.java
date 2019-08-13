package com.platon.browser.req.newtransaction;

import com.platon.browser.req.PageReq;

import lombok.Data;

@Data
public class TransactionListByAddressRequest extends PageReq{
    private String address;
    private String txType;
}