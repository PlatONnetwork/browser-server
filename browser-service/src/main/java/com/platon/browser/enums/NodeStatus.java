package com.platon.browser.enums;

public enum NodeStatus {
    NORMAL(1,"正常"),
    ABNORMAL(2,"异常");
    public int code;
    public String desc;

    NodeStatus ( int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
