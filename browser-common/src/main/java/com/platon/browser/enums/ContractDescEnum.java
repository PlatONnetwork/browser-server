package com.platon.browser.enums;

import org.web3j.platon.ContractAddress;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/2 16:18
 * @Description: 内置合约地址描述枚举
 */
public enum  ContractDescEnum {
    RESTRICTING_PLAN_CONTRACT(ContractAddress.RESTRICTING_PLAN_CONTRACT_ADDRESS,"restricting","system",""),
    STAKING_CONTRACT(ContractAddress.STAKING_CONTRACT_ADDRESS,"staking","system",""),
    SLASH_CONTRACT(ContractAddress.SLASH_CONTRACT_ADDRESS,"slash","system",""),
    PROPOSAL_CONTRACT(ContractAddress.PROPOSAL_CONTRACT_ADDRESS,"proposal","system",""),
    INCENTIVE_POOL_CONTRACT(ContractAddress.INCENTIVE_POOL_CONTRACT_ADDRESS,"incentive","system","");

    public String address;
    public String contractName;
    public String creator;
    public String contractHash;

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

    public static final Set<String> ADDRESSES = new HashSet<>();
    public static final Map<String,ContractDescEnum> MAP = new HashMap<>();

    static {
        Arrays.asList(ContractDescEnum.values()).forEach(innerContractAddEnum-> {
            ADDRESSES.add(innerContractAddEnum.address);
            MAP.put(innerContractAddEnum.address,innerContractAddEnum);
        });
    }
}
