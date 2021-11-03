package com.platon.browser.response.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 * 验证人操作列表返回对象
 *
 * @author zhangrj
 * @file StakingOptRecordListResp.java
 * @description
 * @data 2019年8月31日
 */
public class StakingOptRecordListResp {

    private Long timestamp;         //创建时间

    private String desc;              //操作描述

    private String txHash;            //所属交易

    private Long blockNumber;       //所属区块

    private String type; //类型

    private String id;  //提案id

    private String title;  //提案标题

    private String option;  //投票选型  1：支持；  2：反对；  3弃权

    private String percent;  //处罚百分比

    private BigDecimal amount;  //处罚金额

    private Integer isFire;  //是否踢出列表  0-否，1-是

    private String version;  //版本号

    private String proposalType;  //提案类型  1：文本提案； 2：升级提案；  3参数提案。

    private String beforeRate; //原委托奖励比例

    private String afterRate; //修改后委托奖励比例

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getIsFire() {
        return isFire;
    }

    public void setIsFire(Integer isFire) {
        this.isFire = isFire;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProposalType() {
        return proposalType;
    }

    public void setProposalType(String proposalType) {
        this.proposalType = proposalType;
    }

    public String getBeforeRate() {
        return beforeRate;
    }

    public void setBeforeRate(String beforeRate) {
        this.beforeRate = beforeRate;
    }

    public String getAfterRate() {
        return afterRate;
    }

    public void setAfterRate(String afterRate) {
        this.afterRate = afterRate;
    }

}
