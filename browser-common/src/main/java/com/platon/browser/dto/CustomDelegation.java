package com.platon.browser.dto;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.param.DelegateParam;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description: 委托实体扩展类
 */
@Data
public class CustomDelegation extends Delegation {

    private List<CustomUnDelegation> unDelegations = new ArrayList<>();

    public void updateWithDelegateParam( DelegateParam param,CustomTransaction tx){
        this.setDelegateHas(param.getAmount());
        this.setDelegateLocked("0");
        this.setDelegateReduction("0");
        this.setNodeId(param.getNodeId());
        this.setIsHistory(YesNoEnum.NO.code);
        this.setDelegateAddr(tx.getFrom());
        this.setSequence(tx.getBlockNumber()*10000+tx.getTransactionIndex());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
    }

    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalDelegateHas(){return new BigDecimal(this.getDelegateHas());}
    public BigDecimal decimalDelegateLocked(){return new BigDecimal(this.getDelegateLocked());}
    public BigDecimal decimalDelegateReduction(){return new BigDecimal(this.getDelegateReduction());}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerDelegateHas(){return new BigInteger(this.getDelegateHas());}
    public BigInteger integerDelegateLocked(){return new BigInteger(this.getDelegateLocked());}
    public BigInteger integerDelegateReduction(){return new BigInteger(this.getDelegateReduction());}

    public YesNoEnum getIsHistoryEnum() {
        return YesNoEnum.getEnum(this.getIsHistory());
    }


    public enum YesNoEnum{
        YES(1, "是"),
        NO(2, "否")
        ;
        public int code;
        public String desc;
        YesNoEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map <Integer, YesNoEnum> ENUMS = new HashMap <>();
        static {Arrays.asList(YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static YesNoEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains( YesNoEnum en){return ENUMS.containsValue(en);}
    }
}
