package com.platon.browser.response.home;

/** 首页查询返回对象 */
public class QueryNavigationResp {
	private String type;
	private QueryNavigationStructResp struct;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public QueryNavigationStructResp getStruct() {
		return struct;
	}
	public void setStruct(QueryNavigationStructResp struct) {
		this.struct = struct;
	}

}
