package com.platon.browser.enums;


import com.platon.sdk.contracts.ppos.dto.common.ContractAddress;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * User: dongqile
 * Date: 2019/8/12
 * Time: 16:48
 *
 */
public enum InnerContractAddrEnum {
    RESTRICTING_PLAN_CONTRACT(ContractAddress.RESTRICTING_PLAN_CONTRACT_ADDRESS,"锁仓合约"),
    STAKING_CONTRACT(ContractAddress.STAKING_CONTRACT_ADDRESS,"质押合约"),
    DELEGATE_CONTRACT(ContractAddress.DELEGATE_CONTRACT_ADDRESS,"质押合约"),
    SLASH_CONTRACT(ContractAddress.SLASH_CONTRACT_ADDRESS,"惩罚合约"),
    PROPOSAL_CONTRACT(ContractAddress.PROPOSAL_CONTRACT_ADDRESS,"治理(提案)合约"),
    INCENTIVE_POOL_CONTRACT(ContractAddress.INCENTIVE_POOL_CONTRACT_ADDRESS,"激励池合约"),
    NODE_CONTRACT(ContractAddress.NODE_CONTRACT_ADDRESS,"节点相关合约"),
    REWARD_CONTRACT(ContractAddress.REWARD_CONTRACT_ADDRESS,"领取奖励合约");

    private String address;
    private String desc;

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

    private static final Set<String> ADDRESSES = new HashSet<>();
    public static Set<String> getAddresses(){return ADDRESSES;}

    static {
        Arrays.asList(InnerContractAddrEnum.values()).forEach(innerContractAddEnum-> ADDRESSES.add(innerContractAddEnum.address));
    }
}
