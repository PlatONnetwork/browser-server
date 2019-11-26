package com.platon.browser.res.extra;

import java.util.List;

import lombok.Data;

/**
 * 查询配置文件返回参数
 *  @file QueryConfigResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
@Data
public class QueryConfigResp {

	private List<ModuleConfig> config;
}
