package com.platon.browser.request.staking;

import com.platon.browser.request.PageReq;

/**
 * 历史验证人查询验证对象
 *  @file HistoryStakingListReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class HistoryStakingListReq extends PageReq{
    private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
    
}