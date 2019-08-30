package com.platon.browser.dto;

import com.platon.browser.dao.entity.UnDelegation;
import com.platon.browser.param.UnDelegateParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/14 12:08
 * @Description:
 */
@Data
public class CustomUnDelegation extends UnDelegation {

    public CustomUnDelegation(){
        this.setRedeemLocked(BigDecimal.ZERO.toString());
    }

    public void updateWithUnDelegateParam( UnDelegateParam param, CustomTransaction tx){
        this.setApplyAmount(param.getAmount());
        this.setDelegateAddr(tx.getFrom());
        this.setHash(tx.getHash());
        this.setStakingBlockNum(Long.valueOf(param.getStakingBlockNum()));
        this.setNodeId(param.getNodeId());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
    }

    public StatusEnum getStatusEnum(){
        return StatusEnum.getEnum(this.getStatus());
    }

    public enum StatusEnum{
        EXITING(1, "退出中"),
        EXITED(2, "退回成功");

        public int code;
        public String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static Map <Integer, StatusEnum> ENUMS = new HashMap <>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum( Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains( StatusEnum en){return ENUMS.containsValue(en);}
    }
}
