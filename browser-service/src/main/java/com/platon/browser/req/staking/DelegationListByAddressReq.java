package com.platon.browser.req.staking;

import com.platon.browser.req.PageReq;

/**
 *  地址查询委托请求对象
 *  @file DelegationListByAddressReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class DelegationListByAddressReq extends PageReq{
    private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
}