package com.platon.browserweb.common.enums;

public enum DateType2Enum {
    LASTHREEDAY(7, "最近三天"),
    BEFORETHREEDAY(8, "三天以前"),
    ;

    private Integer code;
    private String desc;

    private DateType2Enum(int code, String desc){
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
