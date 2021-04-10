package com.platon.browser.response.extra;

/**
 * 查询配置文件返回参数
 *  @file QueryConfigResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
public class ConfigDetail {

	private String name;
	
	private String initValue;
	
	private String staleValue;
	
	private String value;
	
	private String startValue;
	
	private String start;
	
	private String endValue;
	
	private String end;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public String getStaleValue() {
		return staleValue;
	}

	public void setStaleValue(String staleValue) {
		this.staleValue = staleValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEndValue() {
		return endValue;
	}

	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
}
