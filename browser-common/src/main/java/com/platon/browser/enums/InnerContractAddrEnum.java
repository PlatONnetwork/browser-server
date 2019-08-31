package com.platon.browser.enums;

import org.web3j.platon.ContractAddress;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2019/8/12
 * \* Time: 16:48
 * \
 */
public enum InnerContractAddrEnum {
    RESTRICTING_PLAN_CONTRACT(ContractAddress.RESTRICTING_PLAN_CONTRACT_ADDRESS,"锁仓合约"),
    STAKING_CONTRACT(ContractAddress.STAKING_CONTRACT_ADDRESS,"质押合约"),
    DELEGATE_CONTRACT(ContractAddress.DELEGATE_CONTRACT_ADDRESS,"质押合约"),
    SLASH_CONTRACT(ContractAddress.SLASH_CONTRACT_ADDRESS,"惩罚合约"),
    PROPOSAL_CONTRACT(ContractAddress.PROPOSAL_CONTRACT_ADDRESS,"治理(提案)合约"),
    INCENTIVE_POOL_CONTRACT(ContractAddress.INCENTIVE_POOL_CONTRACT_ADDRESS,"激励池合约"),
    NODE_CONTRACT(ContractAddress.NODE_CONTRACT_ADDRESS,"节点相关合约");

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
