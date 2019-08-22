package com.platon.browser.dto;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.HexTool;
import lombok.Data;

import java.math.BigInteger;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description:
 */
@Data
public class CustomAddress extends Address {

    public CustomAddress(){
        Date date = new Date();
        this.setUpdateTime(date);
        this.setCreateTime(date);
        /** 初始化默认值 **/
        this.setBalance(BigInteger.ZERO.toString());
        this.setRestrictingBalance(BigInteger.ZERO.toString());
        this.setStakingValue(BigInteger.ZERO.toString());
        this.setDelegateValue(BigInteger.ZERO.toString());
        this.setRedeemedValue(BigInteger.ZERO.toString());
        this.setTxQty(BigInteger.ZERO.intValue());
        this.setTransferQty(BigInteger.ZERO.intValue());
        this.setStakingQty(BigInteger.ZERO.intValue());
        this.setProposalQty(BigInteger.ZERO.intValue());
        this.setCandidateCount(BigInteger.ZERO.intValue());
        this.setDelegateHes(BigInteger.ZERO.toString());
        this.setDelegateLocked(BigInteger.ZERO.toString());
        this.setDelegateUnlock(BigInteger.ZERO.toString());
        this.setDelegateReduction(BigInteger.ZERO.toString());
        this.setContractName("Unknown");
        this.setContractCreate("Unknown");
        this.setContractCreatehash("Unknown");
        this.setRpPlan("");
    }

    /**
     * 更新参数tx中from地址相关的信息
     * @param tx
     */
    public void updateFromWithCustomTransaction(CustomTransaction tx) {
        // 设置地址类型
       // setAddressType(tx.getFrom());


    }
    /**
     * 更新参数tx中to地址相关的信息
     * @param tx
     */
    public void updateToWithCustomTransaction(CustomTransaction tx) {

    }

    /**
     * 更新与地址是from还是to无关的通用属性
     * @param tx
     */
    public void updateWithCustomTransaction(CustomTransaction tx) {
        // 设置地址类型
        if(InnerContractAddrEnum.addresses.contains(this.getAddress())){
            // 内置合约地址
            this.setType(TypeEnum.INNER_CONTRACT.code);
        }else{
            // 主动发起交易的都认为是账户地址因为当前川陀版本无wasm
            this.setType(TypeEnum.ACCOUNT.code);
        }
        // 交易数量加一
        this.setTxQty(this.getTxQty()+1);
    }

    public enum TypeEnum{
        ACCOUNT(1, "账户"),
        CONTRACT(2, "合约"),
        INNER_CONTRACT(3, "内置合约");
        public int code;
        public String desc;
        TypeEnum ( int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map<Integer, TypeEnum> ENUMS = new HashMap<>();
        static {Arrays.asList(TypeEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static TypeEnum getEnum(Integer code){
           return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(TypeEnum en){return ENUMS.containsValue(en);}
     }

}
