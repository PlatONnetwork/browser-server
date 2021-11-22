package com.platon.browser.elasticsearch.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.platon.browser.bean.Receipt;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Accessors(chain = true)
public class Block {

    @JSONField(serialize = false)
    protected List<Transaction> transactions = new ArrayList<>();

    @JSONField(serialize = false)
    private Integer erc20TxQty = 0;

    @JSONField(serialize = false)
    private Integer erc721TxQty = 0;

    private Long num;

    private String hash;

    private String pHash;

    private Date time;

    private Integer size;

    private String gasLimit;

    private String gasUsed;

    private Integer txQty;

    private Integer tranQty;

    private Integer sQty;

    private Integer pQty;

    private Integer dQty;

    private String txGasLimit;

    private String txFee;

    private String nodeName;

    private String nodeId;

    private String reward;

    private String miner;

    private Date creTime;

    private Date updTime;

    private String extra;

    /**
     * 序号ID,用于计算区块下的交易seq自增
     */
    AtomicLong seq;

    @JSONField(serialize = false)
    private List<com.platon.protocol.core.methods.response.Transaction> originTransactions = new ArrayList<>();

    /**
     * key:交易hash,value:回执
     */
    @JSONField(serialize = false)
    private Map<String, Receipt> receiptMap = new HashMap<>();

    /******** 把字符串类数值转换为大浮点数的便捷方法 ********/
    public BigDecimal decimalGasLimit() {
        return new BigDecimal(this.getGasLimit());
    }

    public BigDecimal decimalGasUsed() {
        return new BigDecimal(this.getGasUsed());
    }

    public BigDecimal decimalTxGasLimit() {
        return new BigDecimal(this.getTxGasLimit());
    }

    public BigDecimal decimalTxFee() {
        return new BigDecimal(this.getTxFee());
    }

    public BigDecimal decimalReward() {
        return new BigDecimal(this.getReward());
    }


}