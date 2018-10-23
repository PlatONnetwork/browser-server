/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

public enum OrderStatusEnum {

	ACCEPTED("D","Accepted for bidding 接收到委托单（优先级1）"),
	PENDING_NEW("A","Pending New 准备处理"),
	NEW("0","New 处理中（优先级 2）"),
	PARTIALLY_FILLED("1","Partially filled 部分成交（优先级 4）"),
	FILLED("2","Filled 全部成交（优先级 8）"),
	EXPIRED("C","Expired 非主动撤单（优先级 5）"),
	REJECTED("8","Rejected 拒单（优先级 2）"),
	CANCELED("4","Canceled 主动撤单（优先级 9）"),
	PENDING_CANCEL("6","Pending Cancel 撤单（优先级 12）"),
	SUSPENDED("9","Suspended 挂单"),
	;
	
	private String code;
	private String desc;
	
	private OrderStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static OrderStatusEnum getEnumByCodeValue(String code){
		OrderStatusEnum[] allEnums = values();
		for(OrderStatusEnum e : allEnums){
			if(e.getCode().equals(code)) {
				return e;
			}
		}
        return null;
    }
}
