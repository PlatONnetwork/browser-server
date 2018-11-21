package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum TransactionStatusEnum {
    PENDING(-1,"待处理"),
    SUCCESS(1,"成功"),
    FAILURE(0,"失败");

    public Integer code;
    public String desc;

    TransactionStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<Integer, TransactionStatusEnum> map = new HashMap<>();
    static {
        Arrays.asList(TransactionStatusEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
    }

    public static TransactionStatusEnum getEnum(Integer code){
        return map.get(code);
    }
}
