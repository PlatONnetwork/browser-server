package com.platon.browser.dto;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.enums.InnerContractAddrEnum;
import lombok.Data;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:02
 * @Description:
 */
@Data
public class CustomAddress extends Address {

    public CustomAddress(){
        super();
        Date date = new Date();
        this.setUpdateTime(date);
        this.setCreateTime(date);
        /* 初始化默认值 */
        this.setBalance(BigInteger.ZERO.toString());
        this.setRestrictingBalance(BigInteger.ZERO.toString());
        this.setStakingValue(BigInteger.ZERO.toString());
        this.setDelegateValue(BigInteger.ZERO.toString());
        this.setRedeemedValue(BigInteger.ZERO.toString());
        this.setTxQty(BigInteger.ZERO.intValue());
        this.setTransferQty(BigInteger.ZERO.intValue());
        this.setStakingQty(BigInteger.ZERO.intValue());
        this.setDelegateQty(BigInteger.ZERO.intValue());
        this.setProposalQty(BigInteger.ZERO.intValue());
        this.setCandidateCount(BigInteger.ZERO.intValue());
        this.setDelegateHes(BigInteger.ZERO.toString());
        this.setDelegateLocked(BigInteger.ZERO.toString());
        this.setDelegateUnlock(BigInteger.ZERO.toString());
        this.setDelegateReduction(BigInteger.ZERO.toString());
        this.setContractName("");
        this.setContractCreate("");
        this.setContractCreatehash("");
    }

    /**
     * 更新与地址是from还是to无关的通用属性
     * @param tx 交易
     */
    public void updateWithCustomTransaction(CustomTransaction tx) {
        // 设置地址类型
        if(InnerContractAddrEnum.ADDRESSES.contains(this.getAddress())){
            // 内置合约地址
            this.setType(TypeEnum.INNER_CONTRACT.code);
        }else{
            // 主动发起交易的都认为是账户地址因为当前川陀版本无wasm
            this.setType(TypeEnum.ACCOUNT.code);
        }
        // 交易数量加一
        this.setTxQty(this.getTxQty()+1);
        switch (tx.getTypeEnum()){
            case TRANSFER: // 转账交易，from地址转账交易数加一
                this.setTransferQty(this.getTransferQty()+1); // 累加转账交易总数
                break;
            case INCREASE_STAKING:// 增加自有质押
            case CREATE_VALIDATOR:// 创建验证人
            case EXIT_VALIDATOR:// 退出验证人
            case REPORT_VALIDATOR:// 举报验证人
            case EDIT_VALIDATOR:// 编辑验证人
                this.setStakingQty(this.getStakingQty()+1); // 累加质押交易总数
                break;
            case DELEGATE:// 发起委托
            case UN_DELEGATE:// 撤销委托
                this.setDelegateQty(this.getDelegateQty()+1); // 累加委托交易总数
                break;
            case CANCEL_PROPOSAL:// 取消提案
            case CREATE_PROPOSAL_TEXT:// 创建文本提案
            case CREATE_PROPOSAL_UPGRADE:// 创建升级提案
            case DECLARE_VERSION:// 版本声明
            case VOTING_PROPOSAL:// 提案投票
                this.setProposalQty(this.getProposalQty()+1); // 累加提案交易总数
                break;
		default:
			break;
        }
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

    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerBalance(){return new BigInteger(this.getBalance());}
    public BigInteger integerRestrictingBalance(){return new BigInteger(this.getRestrictingBalance());}
    public BigInteger integerStakingValue(){return new BigInteger(this.getStakingValue());}
    public BigInteger integerDelegateValue(){return new BigInteger(this.getDelegateValue());}
    public BigInteger integerRedeemedValue(){return new BigInteger(this.getRedeemedValue());}
    public BigInteger integerDelegateHes(){return new BigInteger(this.getDelegateHes());}
    public BigInteger integerDelegateLocked(){return new BigInteger(this.getDelegateLocked());}
    public BigInteger integerDelegateUnlock(){return new BigInteger(this.getDelegateUnlock());}
    public BigInteger integerDelegateReduction(){return new BigInteger(this.getDelegateReduction());}
}
