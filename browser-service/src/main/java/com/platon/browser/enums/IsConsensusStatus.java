package com.platon.browser.enums;

public enum IsConsensusStatus {
	YES(1,"是"),
    NO(2,"否");
    public Integer code;
    public String desc;

    IsConsensusStatus ( Integer code, String desc) {
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
