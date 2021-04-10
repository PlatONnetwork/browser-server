package com.platon.browser.enums;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description:
 */
public enum ReceiveTypeEnum {
    CONTRACT(0, "合约"),
    ACCOUNT(1, "账户");

    private int code;
    private String desc;

    ReceiveTypeEnum ( int code, String desc) {
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
