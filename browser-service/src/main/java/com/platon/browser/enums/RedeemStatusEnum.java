package com.platon.browser.enums;

/**
 * 推出验证人前端枚举
 *  @file RedeemStatusEnum.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年9月2日
 */
public enum RedeemStatusEnum {

	EXTING("exting", 1),//退出中
	EXTED("exted" ,2);//已退出
	
	
	private String name;
	
	private Integer code;
	
	RedeemStatusEnum(String name, Integer code) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
