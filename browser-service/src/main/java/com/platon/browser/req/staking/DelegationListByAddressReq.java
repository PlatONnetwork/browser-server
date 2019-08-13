package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

@Data
public class DelegationListByAddressReq extends PageReq{
    private String address;
}