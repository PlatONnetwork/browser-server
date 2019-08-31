package com.platon.browser.res.home;

import java.util.List;

import lombok.Data;

/**
 * 首页验证人列表返回对象
 *  @file StakingListNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class StakingListNewResp {
	private Boolean isRefresh;
	private List<StakingListResp> dataList;
}
