package com.platon.browser.dto;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.UnDelegation;
import com.platon.browser.param.DelegateParam;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description:
 */
@Data
public class CustomDelegation extends Delegation {

    private List<UnDelegation> unDelegations = new ArrayList<>();

    // delegation与staking的关联键
    public String getStakingMapKey(){
        return this.getNodeId()+this.getStakingBlockNum();
    }

    // delegation与un_delegation的关联键
    public String getDelegationMapKey(){
        return this.getDelegateAddr()+this.getNodeId()+this.getStakingBlockNum();
    }

    public void updateWithDelegateParam( DelegateParam param,CustomTransaction tx){
        this.setDelegateHas(param.getAmount());
        this.setDelegateLocked("0");
        this.setDelegateReduction("0");
        this.setNodeId(param.getNodeId());
        this.setIsHistory(YesNoEnum.NO.code);
        this.setDelegateAddr(tx.getFrom());
        this.setSequence(Long.valueOf(param.getStakingBlockNum())*100000+tx.getTransactionIndex());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
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
        private static Map <Integer, CustomStaking.YesNoEnum> ENUMS = new HashMap <>();
        static {Arrays.asList(CustomStaking.YesNoEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static CustomStaking.YesNoEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains( CustomStaking.YesNoEnum en){return ENUMS.containsValue(en);}
    }
}
