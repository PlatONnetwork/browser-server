package com.platon.browser.response.extra;

import java.util.List;

/**
 * 查询配置文件返回参数
 *  @file QueryConfigResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
public class QueryConfigResp {

	private List<ModuleConfig> config;

	public List<ModuleConfig> getConfig() {
		return config;
	}

	public void setConfig(List<ModuleConfig> config) {
		this.config = config;
	}
	
}
