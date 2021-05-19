package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum InternalAddressType {
    FUND_ACCOUNT(0, "基金会账户"),
    RESTRICT_CONTRACT(1, "锁仓合约地址"),
    STAKE_CONTRACT(2, "质押合约"),
    INCENTIVE_CONTRACT(3, "激励池合约"),
    DELEGATE_CONTRACT(6, "委托奖励池合约"),
    OTHER(100, "其它");

    private int code;
    private String desc;

    InternalAddressType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    private static final Map<Integer, InternalAddressType> ENUMS = new HashMap<>();
    static {
        Arrays.asList(InternalAddressType.values()).forEach(en -> ENUMS.put(en.code, en));
    }

    public static InternalAddressType getEnum(Integer code) {
        return ENUMS.get(code);
    }

    public static boolean contains(int code) {
        return ENUMS.containsKey(code);
    }

    public static boolean contains(InternalAddressType en) {
        return ENUMS.containsValue(en);
    }
}
