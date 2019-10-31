//package com.platon.browser.dto;
//
//import com.platon.browser.param.UnDelegateParam;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Auther: Chendongming
// * @Date: 2019/8/14 12:08
// * @Description: 解委托实体扩展类
// */
//@Data
//public class CustomUnDelegation extends UnDelegation {
//
//    public CustomUnDelegation(){
//        super();
//        Date date = new Date();
//        this.setCreateTime(date);
//        this.setUpdateTime(date);
//        this.setRedeemLocked(BigDecimal.ZERO.toString());
//    }
//
//    public void updateWithUnDelegateParam( UnDelegateParam param, CustomTransaction tx){
//        this.setApplyAmount(param.getAmount());
//        this.setDelegateAddr(tx.getFrom());
//        this.setHash(tx.getHash());
//        this.setStakingBlockNum(param.getStakingBlockNum());
//        this.setNodeId(param.getNodeId());
//    }
//
//    /********把字符串类数值转换为大浮点数的便捷方法********/
//    public BigDecimal decimalApplyAmount(){return new BigDecimal(this.getApplyAmount());}
//    public BigDecimal decimalRedeemLocked(){return new BigDecimal(this.getRedeemLocked());}
//    /********把字符串类数值转换为大整数的便捷方法********/
//    public BigInteger integerApplyAmount(){return new BigInteger(this.getApplyAmount());}
//    public BigInteger integerRedeemLocked(){return new BigInteger(this.getRedeemLocked());}
//
//    public StatusEnum getStatusEnum(){
//        return StatusEnum.getEnum(this.getStatus());
//    }
//    /**
//     * 解委托结果类型枚举类：
//     *  1.退出中
//     *  2.退出成功
//     */
//    public enum StatusEnum{
//        EXITING(1, "退出中"),
//        EXITED(2, "退回成功");
//
//        private int code;
//        private String desc;
//        StatusEnum(int code, String desc) {
//            this.code = code;
//            this.desc = desc;
//        }
//        public int getCode(){return code;}
//        public String getDesc(){return desc;}
//        private static final Map <Integer, StatusEnum> ENUMS = new HashMap <>();
//        static {
//            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
//        public static StatusEnum getEnum( Integer code){
//            return ENUMS.get(code);
//        }
//        public static boolean contains(int code){return ENUMS.containsKey(code);}
//        public static boolean contains( StatusEnum en){return ENUMS.containsValue(en);}
//    }
//}
