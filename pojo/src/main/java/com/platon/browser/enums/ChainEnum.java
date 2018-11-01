package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ChainEnum {
    MAIN_NET("1","主网络"),
    TEST_NET("2","测试网络");

    public String code;
    public String desc;

    ChainEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<String, ChainEnum> map = new HashMap<>();
    static {
        Arrays.asList(ChainEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
    }

    public static ChainEnum getEnum(String code){
        return map.get(code);
    }
}
