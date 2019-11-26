package com.platon.browser.res.extra;

import lombok.Data;

/**
 * 查询配置文件返回参数
 *  @file QueryConfigResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
@Data
public class ConfigDetail {

	private String name;
	
	private String initValue;
	
	private String staleValue;
	
	private String value;
	
	private String startValue;
	
	private String start;
	
	private String endValue;
	
	private String end;
	
}
