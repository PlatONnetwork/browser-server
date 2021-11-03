package com.platon.browser.elasticsearch.dto;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.experimental.Accessors;

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

    private String gasPrice;

    private String gasUsed;

    private String gasLimit;

    private String from;

    private String to;

    private String value;

    private Integer type;

    private String cost;

    private Integer toType;

    private Long seq;

    private Date creTime;

    private Date updTime;

    private String input;

    private String info;

    private String erc721TxInfo; // 存储基本信息的json数组串，来自erc721TxList

    private String erc20TxInfo; // 存储基本信息的json数组串，来自erc20TxList

    private String transferTxInfo; // 存储基本信息的json数组串，来自transferTxList

    private String pposTxInfo; // 存储基本信息的json数组串，来自pposTxList

    private String failReason;

    private Integer contractType;

    private String method;

    private String bin;

    private String contractAddress;

    // erc721交易列表
    @JSONField(serialize = false)
    private List<ErcTx> erc721TxList = new ArrayList<>();

    // erc20 交易列表
    @JSONField(serialize = false)
    private List<ErcTx> erc20TxList = new ArrayList<>();

    // 内部转账交易
    @JSONField(serialize = false)
    private List<Transaction> transferTxList = new ArrayList<>();

    // PPOS调用交易
    @JSONField(serialize = false)
    private List<Transaction> pposTxList = new ArrayList<>();

    // 虚拟交易，
    @JSONField(serialize = false)
    private List<Transaction> virtualTransactions = new ArrayList<>();

    /******** 把字符串类数值转换为大浮点数的便捷方法 ********/
    public BigDecimal decimalGasLimit() {
        return new BigDecimal(this.getGasLimit());
    }

    public BigDecimal decimalGasPrice() {
        return new BigDecimal(this.getGasPrice());
    }

    public BigDecimal decimalGasUsed() {
        return new BigDecimal(this.getGasUsed());
    }

    public BigDecimal decimalValue() {
        return new BigDecimal(this.getValue());
    }

    public BigDecimal decimalCost() {
        return new BigDecimal(this.getCost());
    }

    public enum TypeEnum {
        /**
         * 0-转账
         */
        TRANSFER(0, "转账"),
        /**
         * 合约销毁
         */
        CONTRACT_EXEC_DESTROY(21, "合约销毁"),
        /**
         * 1-EVM合约发布(合约创建)
         */
        EVM_CONTRACT_CREATE(1, "EVM合约发布(合约创建)"),
        /**
         * 2-合约调用(合约执行)
         */
        CONTRACT_EXEC(2, "合约调用(合约执行)"),
        /**
         * 3-WASM合约发布(合约创建)
         */
        WASM_CONTRACT_CREATE(3, "WASM合约发布(合约创建)"),
        /**
         * 4-MPC交易
         */
        OTHERS(4, "其他"),
        /**
         * 5-MPC交易
         */
        MPC(5, "MPC交易"),
        /**
         * 6-ERC20合约发布(合约创建)
         */
        ERC20_CONTRACT_CREATE(6, "ERC20合约发布(合约创建)"),
        /**
         * 7-ERC20合约调用(合约执行)
         */
        ERC20_CONTRACT_EXEC(7, "ERC20合约调用(合约执行)"),
        /**
         * 8-ERC721合约发布(合约创建)
         */
        ERC721_CONTRACT_CREATE(8, "ERC721合约发布(合约创建)"),
        /**
         * 9-ERC721合约调用(合约执行)
         */
        ERC721_CONTRACT_EXEC(9, "ERC721合约调用(合约执行)"),
        /**
         * 1000-发起质押(创建验证人)
         */
        STAKE_CREATE(1000, "发起质押(创建验证人)"),
        /**
         * 1001-修改质押信息(编辑验证人)
         */
        STAKE_MODIFY(1001, "修改质押信息(编辑验证人)"),
        /**
         * 1002-增持质押(增加自有质押)
         */
        STAKE_INCREASE(1002, "增持质押(增加自有质押)"),
        /**
         * 1003-撤销质押(退出验证人)
         */
        STAKE_EXIT(1003, "撤销质押(退出验证人)"),
        /**
         * 1004-发起委托(委托)
         */
        DELEGATE_CREATE(1004, "发起委托(委托)"),
        /**
         * 1005-减持/撤销委托(赎回委托)
         */
        DELEGATE_EXIT(1005, "减持/撤销委托(赎回委托)"),
        /**
         * 2000-提交文本提案(创建提案)
         */
        PROPOSAL_TEXT(2000, "提交文本提案(创建提案)"),
        /**
         * 2001-提交升级提案(创建提案)
         */
        PROPOSAL_UPGRADE(2001, "提交升级提案(创建提案)"),
        /**
         * 2002-提交参数提案(创建提案)
         */
        PROPOSAL_PARAMETER(2002, "提交参数提案(创建提案)"),
        /**
         * 2005-提交取消提案
         */
        PROPOSAL_CANCEL(2005, "提交取消提案"),
        /**
         * 2003-给提案投票(提案投票)
         */
        PROPOSAL_VOTE(2003, "给提案投票(提案投票)"),
        /**
         * 2004-版本声明
         */
        VERSION_DECLARE(2004, "版本声明"),
        /**
         * 3000-举报多签(举报验证人)
         */
        REPORT(3000, "举报多签(举报验证人)"),
        /**
         * 4000-创建锁仓计划(创建锁仓)
         */
        RESTRICTING_CREATE(4000, "创建锁仓计划(创建锁仓)"),
        /**
         * 5000-领取奖励
         */
        CLAIM_REWARDS(5000, "领取奖励");

        private static Map<Integer, TypeEnum> map = new HashMap<>();

        static {
            Arrays.asList(TypeEnum.values()).forEach(typeEnum -> map.put(typeEnum.code, typeEnum));
        }

        public static TypeEnum getEnum(int code) {
            return map.get(code);
        }

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
    }

    /**
     * 交易结果成败枚举类： 1.成功 2.失败
     */
    public enum StatusEnum {
        SUCCESS(1, "成功"),
        FAILURE(2, "失败");

        private int code;

        private String desc;

        StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        private static final Map<Integer, StatusEnum> ENUMS = new HashMap<>();

        static {
            Arrays.asList(StatusEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static StatusEnum getEnum(Integer code) {
            return ENUMS.get(code);
        }

        public static boolean contains(int code) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains(StatusEnum en) {
            return ENUMS.containsValue(en);
        }
    }

    /**
     * 交易接收者类型(to是合约还是账户):地址类型 :1账号,2内置合约 ,3EVM合约,4WASM合约
     */
    public enum ToTypeEnum {
        ACCOUNT(1, "账户"),
        INNER_CONTRACT(2, "内置合约"),
        EVM_CONTRACT(3, "EVM合约"),
        WASM_CONTRACT(4, "WASM合约"),
        ERC20_CONTRACT(5, "ERC20-EVM合约"),
        ERC721_CONTRACT(6, "ERC721-EVM合约");

        private int code;

        private String desc;

        ToTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return this.desc;
        }

        private static final Map<Integer, ToTypeEnum> ENUMS = new HashMap<>();

        static {
            Arrays.asList(ToTypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static ToTypeEnum getEnum(Integer code) {
            return ENUMS.get(code);
        }

        public static boolean contains(int code) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains(ToTypeEnum en) {
            return ENUMS.containsValue(en);
        }
    }

    /**
     * 获取当前交易的交易类型枚举
     *
     * @return
     */
    @JsonIgnore
    public TypeEnum getTypeEnum() {
        return TypeEnum.getEnum(this.getType());
    }

    /**
     * 根据类型获取交易参数信息对象
     *
     * @return
     */
    @JsonIgnore
    public <T> T getTxParam(Class<T> clazz) {
        return JSON.parseObject(this.getInfo(), clazz);
    }

}