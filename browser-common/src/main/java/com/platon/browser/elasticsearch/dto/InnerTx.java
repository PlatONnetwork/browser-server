package com.platon.browser.elasticsearch.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.*;

@Data
@Accessors(chain = true)
public class InnerTx {
    private Long id;
    //交易hash
    private String hash;
    //区块号
    private Long num;
    //序列
    private Integer index;
    //日期
    private Date time;
    private String from;
    //to地址也就是对应的实际接收地址
    private String to;
    //交易value
    private String value;
    //token名称
    private String tokenName;
    //token合约地址
    private String tokenAddr;
    //精度
    private String decimal;
    //符号
    private String symbol;
    private Integer status;
    //原始事件信息
    private String info;
    //实际转账地址
    private String transValue;
    private Long seq;
    private Date creTime;
    private Date updTime;


    // 虚拟交易，
    @JsonIgnore
    private List<InnerTx> virtualTransactions=new ArrayList<>();

    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalValue(){return new BigDecimal(this.getValue());}
    public BigDecimal decimalTransValue(){return new BigDecimal(this.getTransValue());}

    /**
     * 交易结果成败枚举类：
     *  1.成功
     *  2.失败
     */
    public enum StatusEnum{
        SUCCESS(1, "成功"),
        FAILURE(2, "失败")
        ;
        private int code;
        private String desc;
        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, StatusEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(StatusEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static StatusEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(StatusEnum en){return ENUMS.containsValue(en);}
    }


    /**
     * 根据类型获取交易参数信息对象
     * @return
     */
    @JsonIgnore
    public <T> T getTxParam (Class<T> clazz) {
        return JSON.parseObject(this.getInfo(), clazz);
    }


}