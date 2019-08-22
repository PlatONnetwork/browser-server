package com.platon.browser.enums;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 18:04
 */
public enum AddressEnum {

    ACCOUNT(1, "账户"),
    CONTRACT(2, "合约"),
    INNER_CONTRACT(3, "内置合约");

    public int code;
    public String desc;

    AddressEnum ( int code, String desc) {
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
