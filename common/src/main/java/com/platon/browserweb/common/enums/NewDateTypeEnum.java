package com.platon.browserweb.common.enums;

public enum NewDateTypeEnum {
    TODAY(0, "12小时"),
    WEEK(1, "周，7天"),
    MONTH(2, "月"),
    MONTH3(3, "季"),
    YEAR(4, "年"),
    NOW(5, "至今"),
    USER_DEFINED(6, "自定义时间"),;


    private Integer code;
    private String desc;

    private NewDateTypeEnum(int code, String desc) {
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

