package com.platon.browser.request.address;

import com.platon.browser.request.PageReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *  查询地址锁仓详情请求对象
 *  @file QueryDetailRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class QueryRPPlanDetailRequest extends PageReq{
    @NotBlank(message = "{address not null}")
    @Size(min = 42,max = 42)
    private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.toLowerCase();
	}
    
}