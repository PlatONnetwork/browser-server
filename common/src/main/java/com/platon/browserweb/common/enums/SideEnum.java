/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

public enum SideEnum {

	BUY("1","买入"),
	SELL("2","卖出")
	;

	private String code;
	private String desc;

	private SideEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static SideEnum getEnumByCodeValue(String code){
		SideEnum[] allEnums = values();
		for(SideEnum e : allEnums){
			if(e.getCode().equals(code)) {
				return e;
			}
		}
        return null;
    }
}
