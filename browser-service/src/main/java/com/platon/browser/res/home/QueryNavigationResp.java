package com.platon.browser.res.home;

import lombok.Data;

/** 首页查询返回对象 */
@Data
public class QueryNavigationResp {
	private String type;
	private QueryNavigationStructResp struct;

}
