/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

public enum OrderTypeEnum {
    MARKET("市价"),
    LIMIT("限价"),
    AMOUNT_MARKET("金额市价"),
    AMOUNT_LIMIT("金额限价");

    private String desc;

    private OrderTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderTypeEnum getEnumByCodeValue(String code){
        OrderTypeEnum[] allEnums = values();
        for(OrderTypeEnum e : allEnums){
            if(e.name().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
