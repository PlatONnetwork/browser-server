package com.platon.browser.enums;

import org.web3j.tx.PlatOnContract;

import java.util.*;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2019/8/12
 * \* Time: 16:48
 * \
 */
public enum InnerContractAddrEnum {
    RESTRICTING_PLAN_CONTRACT(PlatOnContract.RESTRICTING_PLAN_CONTRACT_ADDRESS,"锁仓合约"),
    STAKING_CONTRACT(PlatOnContract.STAKING_CONTRACT_ADDRESS,"质押合约"),
    SLASH_CONTRACT(PlatOnContract.SLASH_CONTRACT_ADDRESS,"惩罚合约"),
    PROPOSAL_CONTRACT(PlatOnContract.PROPOSAL_CONTRACT_ADDRESS,"治理(提案)合约"),
    INCENTIVE_POOL_CONTRACT("0x1000000000000000000000000000000000000003","激励池合约"),
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

    public static Set<String> addresses = new HashSet<>();

    static {
        Arrays.asList(InnerContractAddrEnum.values()).forEach(innerContractAddEnum-> addresses.add(innerContractAddEnum.address));
    }
}
