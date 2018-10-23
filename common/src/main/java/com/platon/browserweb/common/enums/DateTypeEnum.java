/*
 * Copyright (c) 2017. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.enums;

public enum DateTypeEnum {
    TODAY(1, "近一日"),
    WEEK(7, "近一周"),
    MONTH1(30, "近一月"),
    MONTH3(90, "近三月"),
    MONTH6(180, "近六月"),
    YEAR(360, "近一年"),
    USER_DEFINED(0, "自定义时间"),
    ;


    private Integer code;
    private String desc;

    private DateTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
