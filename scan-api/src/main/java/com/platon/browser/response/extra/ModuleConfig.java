package com.platon.browser.response.extra;

import java.util.List;

/**
 * 查询配置文件返回参数
 *  @file QueryConfigResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
public class ModuleConfig {

	private String module;
	
	private List<ConfigDetail> detail;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<ConfigDetail> getDetail() {
		return detail;
	}

	public void setDetail(List<ConfigDetail> detail) {
		this.detail = detail;
	}
	
}
