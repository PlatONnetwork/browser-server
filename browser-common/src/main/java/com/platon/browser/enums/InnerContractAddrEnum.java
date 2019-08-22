package com.platon.browser.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2019/8/12
 * \* Time: 16:48
 * \
 */
public enum InnerContractAddrEnum {
    LOCK_CONTRACT("0x1000000000000000000000000000000000000001","锁仓合约"),
    STAKING_CONTRACT("0x1000000000000000000000000000000000000002","质押合约"),
    EXCITATION_CONTRACT("0x1000000000000000000000000000000000000003","激励池合约"),
    PUNISH_CONTRACT("0x1000000000000000000000000000000000000004","惩罚合约"),
    GOVERNANCE_CONTRACT("0x1000000000000000000000000000000000000005","治理合约"),
    FOUNDATION("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219","基金会");

    public String address;
    public String desc;

    InnerContractAddrEnum(String address, String desc) {
        this.address = address;
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public String getDesc() {
        return desc;
    }

    public static List<String> addresses = new ArrayList <>();

    static {
        Arrays.asList(InnerContractAddrEnum.values()).forEach(innerContractAddEnum-> addresses.add(innerContractAddEnum.address));
    }
}
