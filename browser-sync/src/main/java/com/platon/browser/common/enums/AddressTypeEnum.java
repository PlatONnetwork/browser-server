package com.platon.browser.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum AddressTypeEnum {
    ACCOUNT(1, "账户"),
    CONTRACT(2, "合约"),
    INNER_CONTRACT(3, "内置合约");
	private int code;
    private String desc;
    AddressTypeEnum ( int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode(){return code;}
    public String getDesc(){return desc;}
    private static final Map<Integer, AddressTypeEnum> ENUMS = new HashMap<>();
    static {Arrays.asList(AddressTypeEnum.values()).forEach(en->ENUMS.put(en.code,en));}
    public static AddressTypeEnum getEnum(Integer code){
       return ENUMS.get(code);
    }
    public static boolean contains(int code){return ENUMS.containsKey(code);}
    public static boolean contains(AddressTypeEnum en){return ENUMS.containsValue(en);}
}
