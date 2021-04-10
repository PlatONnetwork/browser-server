package com.platon.browser.enums;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.InnerContract;
import com.platon.parameters.NetworkParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/2 16:18
 * @Description: 内置合约地址描述枚举
 */
@Slf4j
public enum ContractDescEnum {
    RESTRICTING_PLAN_CONTRACT(NetworkParameters.getPposContractAddressOfRestrctingPlan()),
    STAKING_CONTRACT(NetworkParameters.getPposContractAddressOfStaking()),
    INCENTIVE_POOL_CONTRACT(NetworkParameters.getPposContractAddressOfIncentivePool()),
    SLASH_CONTRACT(NetworkParameters.getPposContractAddressOfSlash()),
    PROPOSAL_CONTRACT(NetworkParameters.getPposContractAddressOfProposal()),
    REWARD_CONTRACT(NetworkParameters.getPposContractAddressOfReward());

    private String address;

    private String contractName;

    private String creator;

    private String contractHash;

    private static final Set<String> ADDRESSES = new HashSet<>();

    private static final Map<String, ContractDescEnum> MAP = new HashMap<>();

    ContractDescEnum(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContractHash() {
        return contractHash;
    }

    public void setContractHash(String contractHash) {
        this.contractHash = contractHash;
    }

    public static Set<String> getAddresses() {
        return ADDRESSES;
    }

    public static Map<String, ContractDescEnum> getMap() {
        return MAP;
    }

    @Autowired
    ResourceLoader resourceLoader;

    static {
        try {
            // 加载内置合约文件
            String fileString = ResourceUtil.readUtf8Str("constact/InnerContract.json");
            List<InnerContract> innerContractList = JSONUtil.toList(fileString, InnerContract.class);
            for (ContractDescEnum value : ContractDescEnum.values()) {
                for (InnerContract innerContract : innerContractList) {
                    if (value.name().equalsIgnoreCase(innerContract.getContractDescEnumName())) {
                        value.setContractName(innerContract.getContractName());
                        value.setCreator(innerContract.getCreator());
                        value.setContractHash(innerContract.getContractHash());
                    }
                }
            }
            Arrays.asList(ContractDescEnum.values()).forEach(innerContractAddEnum -> {
                ADDRESSES.add(innerContractAddEnum.address);
                MAP.put(innerContractAddEnum.address, innerContractAddEnum);
            });
        } catch (Exception e) {
            log.error("初始化内置合约失败", e);
        }
    }
}
