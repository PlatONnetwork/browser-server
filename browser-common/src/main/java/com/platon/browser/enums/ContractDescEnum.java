package com.platon.browser.enums;


import com.platon.sdk.contracts.ppos.dto.common.ContractAddress;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/2 16:18
 * @Description: 内置合约地址描述枚举
 */
public enum  ContractDescEnum {
    RESTRICTING_PLAN_CONTRACT(ContractAddress.RESTRICTING_PLAN_CONTRACT_ADDRESS,"RestrictingContract","system",""),
    STAKING_CONTRACT(ContractAddress.STAKING_CONTRACT_ADDRESS,"StakingContract","system",""),
    INCENTIVE_POOL_CONTRACT(ContractAddress.INCENTIVE_POOL_CONTRACT_ADDRESS,"RewardManagerPool","system",""),
    SLASH_CONTRACT(ContractAddress.SLASH_CONTRACT_ADDRESS,"SlashContract","system",""),
    PROPOSAL_CONTRACT(ContractAddress.PROPOSAL_CONTRACT_ADDRESS,"GovContract","system",""),
	REWARD_CONTRACT(ContractAddress.REWARD_CONTRACT_ADDRESS,"DelegateRewardPool","system","");

    private String address;
    private String contractName;
    private String creator;
    private String contractHash;

    ContractDescEnum(String address, String contractName, String creator,String contractHash) {
        this.address = address;
        this.contractName = contractName;
        this.creator = creator;
        this.contractHash=contractHash;
    }

    public String getAddress() {
        return address;
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
        Arrays.asList(ContractDescEnum.values()).forEach(innerContractAddEnum-> {
            ADDRESSES.add(innerContractAddEnum.address);
            MAP.put(innerContractAddEnum.address,innerContractAddEnum);
        });
    }
}
