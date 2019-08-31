package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

/**
 *  地址查询委托请求对象
 *  @file DelegationListByAddressReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class DelegationListByAddressReq extends PageReq{
    private String address;
}