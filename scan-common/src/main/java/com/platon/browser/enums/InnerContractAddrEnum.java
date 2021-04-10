package com.platon.browser.enums;


import com.platon.parameters.NetworkParameters;

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
    RESTRICTING_PLAN_CONTRACT(NetworkParameters.getPposContractAddressOfRestrctingPlan(),"锁仓合约"),
    STAKING_CONTRACT(NetworkParameters.getPposContractAddressOfStaking(),"质押合约"),
    DELEGATE_CONTRACT(NetworkParameters.getPposContractAddressOfStaking(),"质押合约"),
    SLASH_CONTRACT(NetworkParameters.getPposContractAddressOfSlash(),"惩罚合约"),
    PROPOSAL_CONTRACT(NetworkParameters.getPposContractAddressOfProposal(),"治理(提案)合约"),
    INCENTIVE_POOL_CONTRACT(NetworkParameters.getPposContractAddressOfIncentivePool(),"激励池合约"),
    NODE_CONTRACT(NetworkParameters.getPposContractAddressOfStaking(),"节点相关合约"),
    REWARD_CONTRACT(NetworkParameters.getPposContractAddressOfReward(),"领取奖励合约");

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
