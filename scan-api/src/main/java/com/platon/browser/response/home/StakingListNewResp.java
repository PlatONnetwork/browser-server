package com.platon.browser.response.home;

import java.util.List;

/**
 * 首页验证人列表返回对象
 *  @file StakingListNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class StakingListNewResp {
	private Boolean isRefresh;
	private List<StakingListResp> dataList;
	public Boolean getIsRefresh() {
		return isRefresh;
	}
	public void setIsRefresh(Boolean isRefresh) {
		this.isRefresh = isRefresh;
	}
	public List<StakingListResp> getDataList() {
		return dataList;
	}
	public void setDataList(List<StakingListResp> dataList) {
		this.dataList = dataList;
	}
	
}
