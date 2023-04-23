package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum AddressTypeEnum {
    ACCOUNT(1, "账户"),
    INNER_CONTRACT(2, "内置合约"),
    EVM_CONTRACT(3, "EVM合约"),
    WASM_CONTRACT(4, "WASM合约"),
    ERC20_EVM_CONTRACT(5, "ERC20合约"),
    ERC721_EVM_CONTRACT(6, "ERC721合约"),
    ERC1155_EVM_CONTRACT(7, "ERC1155合约");

    private int code;
    private String desc;

    AddressTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    private static final Map<Integer, AddressTypeEnum> ENUMS = new HashMap<>();
    static {
        Arrays.asList(AddressTypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
    }

    public static AddressTypeEnum getEnum(Integer code) {
        return ENUMS.get(code);
    }

    public static boolean contains(int code) {
        return ENUMS.containsKey(code);
    }

    public static boolean contains(AddressTypeEnum en) {
        return ENUMS.containsValue(en);
    }
}
