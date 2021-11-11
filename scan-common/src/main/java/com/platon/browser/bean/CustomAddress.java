package com.platon.browser.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.platon.browser.dao.entity.Address;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description: 地址实体扩展类
 */
@Data
public class CustomAddress extends Address {

    public CustomAddress() {
        super();
        Date date = new Date();
        this.setUpdateTime(date);
        this.setCreateTime(date);
        /* 初始化默认值 */
        this.setBalance(BigDecimal.ZERO);
        this.setRestrictingBalance(BigDecimal.ZERO);
        this.setStakingValue(BigDecimal.ZERO);
        this.setDelegateValue(BigDecimal.ZERO);
        this.setRedeemedValue(BigDecimal.ZERO);
        this.setTxQty(BigInteger.ZERO.intValue());
        this.setTransferQty(BigInteger.ZERO.intValue());
        this.setStakingQty(BigInteger.ZERO.intValue());
        this.setDelegateQty(BigInteger.ZERO.intValue());
        this.setProposalQty(BigInteger.ZERO.intValue());
        this.setCandidateCount(BigInteger.ZERO.intValue());
        this.setDelegateHes(BigDecimal.ZERO);
        this.setDelegateLocked(BigDecimal.ZERO);
        this.setContractName("");
        this.setContractCreate("");
        this.setContractCreatehash("");
    }

    /**
     * 地址类型 :1账号,2内置合约 ,3EVM合约,4WASM合约
     */
    public enum TypeEnum {
        ACCOUNT(1, "账号"),
        INNER_CONTRACT(2, "内置合约"),
        EVM(3, "3EVM合约"),
        WASM(4, "WASM合约"),
        ERC20_EVM(5, "ERC20合约"),
        ERC721_EVM(6, "ERC721合约");

        private int code;
        private String desc;

        TypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        private static final Map<Integer, TypeEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static TypeEnum getEnum(Integer code) {
            return ENUMS.get(code);
        }

        public static boolean contains(int code) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains(CustomStaking.StatusEnum en) {
            return ENUMS.containsValue(en);
        }
    }
}
