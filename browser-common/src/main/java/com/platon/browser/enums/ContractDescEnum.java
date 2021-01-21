package com.platon.browser.enums;

import com.alaya.parameters.NetworkParameters;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/2 16:18
 * @Description: 内置合约地址描述枚举
 */
public enum  ContractDescEnum {
    RESTRICTING_PLAN_CONTRACT("RestrictingContract","system",""),
    STAKING_CONTRACT("StakingContract","system",""),
    INCENTIVE_POOL_CONTRACT("RewardManagerPool","system",""),
    SLASH_CONTRACT("SlashingContract","system",""),
    PROPOSAL_CONTRACT("GovContract","system",""),
	REWARD_CONTRACT("DelegateRewardPool","system","");

    private String contractName;
    private String creator;
    private String contractHash;

    ContractDescEnum(String contractName, String creator,String contractHash) {
        this.contractName = contractName;
        this.creator = creator;
        this.contractHash=contractHash;
    }

    public String getAddress() {
        switch (this){
            case RESTRICTING_PLAN_CONTRACT: return NetworkParameters.getPposContractAddressOfRestrctingPlan(NetworkParameters.CurrentNetwork.getChainId());
            case STAKING_CONTRACT: return NetworkParameters.getPposContractAddressOfStaking(NetworkParameters.CurrentNetwork.getChainId());
            case INCENTIVE_POOL_CONTRACT: return NetworkParameters.getPposContractAddressOfIncentivePool(NetworkParameters.CurrentNetwork.getChainId());
            case SLASH_CONTRACT: return NetworkParameters.getPposContractAddressOfSlash(NetworkParameters.CurrentNetwork.getChainId());
            case PROPOSAL_CONTRACT: return NetworkParameters.getPposContractAddressOfProposal(NetworkParameters.CurrentNetwork.getChainId());
            case REWARD_CONTRACT: return NetworkParameters.getPposContractAddressOfReward(NetworkParameters.CurrentNetwork.getChainId());
        }
        return null;
    }

    public String getContractName() {
        return contractName;
    }

    public String getCreator() {
        return creator;
    }

    public String getContractHash() {
        return contractHash;
    }

    private static final Set<String> ADDRESSES = new HashSet<>();
    public static Set<String> getAddresses(){return ADDRESSES;}
    private static final Map<String,ContractDescEnum> MAP = new HashMap<>();
    public static Map<String,ContractDescEnum> getMap(){return MAP;}
    static {
        Arrays.asList(ContractDescEnum.values()).forEach(e-> {
            ADDRESSES.add(e.getAddress());
            MAP.put(e.getAddress(),e);
        });
    }
}
