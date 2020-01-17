package com.platon.browser.enums;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description:
 */
public enum ContractTypeEnum {
    EVM(0, "EVM"),
    WASM(1, "WASM");

    private int code;
    private String desc;

    ContractTypeEnum(int code, String desc) {
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
