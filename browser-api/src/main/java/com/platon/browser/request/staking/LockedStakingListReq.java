package com.platon.browser.request.staking;

import com.platon.browser.request.PageReq;

/**
 *  锁定验证人列表请求对象
 *  @file AliveStakingListReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class LockedStakingListReq extends PageReq{
    private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}