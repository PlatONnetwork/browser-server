package com.platon.browser.enums;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2019/8/12
 * \* Time: 16:48
 * \
 */
public enum InnerContractAddEnum {

    LOCKCONTRACT("0x1000000000000000000000000000000000000001","锁仓合约"),
    STAKINGCONTRACT("0x1000000000000000000000000000000000000002","质押合约"),
    EXCITATIONCONTRACT("0x1000000000000000000000000000000000000003","激励池合约"),
    PUNISHCONTRACT("0x1000000000000000000000000000000000000004","惩罚合约"),
    GOVERNMENTCONTRACT("0x1000000000000000000000000000000000000005","治理合约"),
    FOUNDATION("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219","基金会");





    public String address;
    public String desc;

    InnerContractAddEnum ( String address, String desc) {
        this.address = address;
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public String getDesc() {
        return desc;
    }
}