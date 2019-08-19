package com.platon.browser.enums;

public enum StakingStatus {
	CANDIDATE(1,"候选中"),
    ABORTING(2,"退出中"),
    EXITED(3,"已推出");
    public Integer code;
    public String desc;

    StakingStatus ( Integer code, String desc) {
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
