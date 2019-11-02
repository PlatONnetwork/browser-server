package com.platon.browser.elasticsearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Transaction {
    private Long id;
    private String hash;
    private String bHash;
    private Long num;
    private Integer index;
    private Date time;
    private String nonce;
    private Integer status;
    private BigDecimal gasPrice;
    private BigDecimal gasUsed;
    private BigDecimal gasLimit;
    private String from;
    private String to;
    private BigDecimal value;
    private Integer type;
    private BigDecimal cost;
    private Integer toType;
    private Long seq;
    private Date creTime;
    private Date updTime;
    private String input;
    private String info;
    private String failReason;

    public enum TypeEnum {
        TRANSFER(0, "转账"),
        CONTRACT_CREATE(1,"合约发布(合约创建)"),
        CONTRACT_EXEC(2,"合约调用(合约执行)"),
        OTHERS(4,"其他"),
        MPC(5,"MPC交易"),
        STAKE_CREATE(1000,"发起质押(创建验证人)"),
        STAKE_MODIFY(1001,"修改质押信息(编辑验证人)"),
        STAKE_INCREASE(1002,"增持质押(增加自有质押)"),
        STAKE_EXIT(1003,"撤销质押(退出验证人)"),
        DELEGATE_CREATE(1004,"发起委托(委托)"),
        DELEGATE_EXIT(1005,"减持/撤销委托(赎回委托)"),
        PROPOSAL_TEXT(2000,"提交文本提案(创建提案)"),
        PROPOSAL_UPGRADE(2001,"提交升级提案(创建提案)"),
        PROPOSAL_PARAMETER(2002,"提交参数提案(创建提案)"),
        PROPOSAL_CANCEL(2005,"提交取消提案"),
        PROPOSAL_VOTE(2003,"给提案投票(提案投票)"),
        VERSION_DECLARE(2004,"版本声明"),
        REPORT(3000,"举报多签(举报验证人)"),
        RESTRICTING_CREATE(4000,"创建锁仓计划(创建锁仓)"),
        MULTI_SIGN(11,"区块双签"),
        ;
        private static Map<Integer, TypeEnum> map = new HashMap<>();
        static {
            Arrays.asList(TypeEnum.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
        }
        public static TypeEnum getEnum(int code){
            return map.get(code);
        }
        private int code;
        private String desc;
        TypeEnum ( int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }
    /**
     * 交易结果成败枚举类：
     *  1.成功
     *  2.失败
     */
    public enum StatusEnum{
        SUCCESS(1, "成功"),
        FAILURE(0, "失败")
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
     * 交易接收者类型(to是合约还是账户):1合约,2账户
     */
    public enum ToTypeEnum{
        CONTRACT(1, "合约"),
        ACCOUNT(2, "账户")
        ;
        private int code;
        private String desc;
        ToTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode(){return code;}
        public String getDesc(){return desc;}
        private static final Map<Integer, ToTypeEnum> ENUMS = new HashMap<>();
        static {
            Arrays.asList(ToTypeEnum.values()).forEach(en->ENUMS.put(en.code,en));}
        public static ToTypeEnum getEnum(Integer code){
            return ENUMS.get(code);
        }
        public static boolean contains(int code){return ENUMS.containsKey(code);}
        public static boolean contains(ToTypeEnum en){return ENUMS.containsValue(en);}
    }
}