package com.platon.browser.enums;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description:
 */
public enum ContractTypeEnum {
    INNER(0, "INNER"),
    EVM(1, "EVM"),
    WASM(2, "WASM"),
    UNKNOWN(3, "UNKNOWN");

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
