package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/12 19:53
 * @Description:
 */
public enum ContractTypeEnum {
    INNER(0, "INNER"),
    EVM(1, "EVM"),
    WASM(2, "WASM"),
    UNKNOWN(3, "UNKNOWN"),
    ERC20_EVM(4, "ERC20_EVM"),
    ERC721_EVM(5, "ERC721_EVM"),
    ERC1155_EVM(6, "ERC1155_EVM");

    private int code;
    private String desc;

    ContractTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ContractTypeEnum> ENUMS = new HashMap<>();
    static {
        Arrays.asList(ContractTypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
    }

    public static ContractTypeEnum getEnum(Integer code) {
        return ENUMS.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
