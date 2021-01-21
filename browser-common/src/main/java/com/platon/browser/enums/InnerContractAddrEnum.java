package com.platon.browser.enums;


import com.alaya.parameters.NetworkParameters;

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
    RESTRICTING_PLAN_CONTRACT("锁仓合约"),
    STAKING_CONTRACT("质押合约"),
    DELEGATE_CONTRACT("质押合约"),
    NODE_CONTRACT("节点相关合约"),
    SLASH_CONTRACT("惩罚合约"),
    PROPOSAL_CONTRACT("治理(提案)合约"),
    INCENTIVE_POOL_CONTRACT("激励池合约"),
    REWARD_CONTRACT("领取奖励合约");

    private String desc;

    InnerContractAddrEnum(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        switch (this){
            case RESTRICTING_PLAN_CONTRACT: return NetworkParameters.getPposContractAddressOfRestrctingPlan(NetworkParameters.CurrentNetwork.getChainId());
            case STAKING_CONTRACT:
            case DELEGATE_CONTRACT:
            case NODE_CONTRACT: return NetworkParameters.getPposContractAddressOfStaking(NetworkParameters.CurrentNetwork.getChainId());
            case SLASH_CONTRACT: return NetworkParameters.getPposContractAddressOfSlash(NetworkParameters.CurrentNetwork.getChainId());
            case PROPOSAL_CONTRACT: return NetworkParameters.getPposContractAddressOfProposal(NetworkParameters.CurrentNetwork.getChainId());
            case INCENTIVE_POOL_CONTRACT: return NetworkParameters.getPposContractAddressOfIncentivePool(NetworkParameters.CurrentNetwork.getChainId());
            case REWARD_CONTRACT: return NetworkParameters.getPposContractAddressOfReward(NetworkParameters.CurrentNetwork.getChainId());
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    private static final Set<String> ADDRESSES = new HashSet<>();
    public static Set<String> getAddresses(){return ADDRESSES;}

    static {
        Arrays.asList(InnerContractAddrEnum.values()).forEach(e-> ADDRESSES.add(e.getAddress()));
    }
}
