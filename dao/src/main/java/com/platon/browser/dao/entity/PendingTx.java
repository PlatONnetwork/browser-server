package com.platon.browser.dao.entity;

import java.util.Date;

public class PendingTx {
    private String hash;

    private String from;

    private String to;

    private String energonUsed;

    private String energonLimit;

    private String energonPrice;

    private Date timestamp;

    private String chainId;

    private String txType;

    private String value;

    private Date createTime;

    private Date updateTime;

    private String input;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from == null ? null : from.trim();
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to == null ? null : to.trim();
    }

    public String getEnergonUsed() {
        return energonUsed;
    }

    public void setEnergonUsed(String energonUsed) {
        this.energonUsed = energonUsed == null ? null : energonUsed.trim();
    }

    public String getEnergonLimit() {
        return energonLimit;
    }

    public void setEnergonLimit(String energonLimit) {
        this.energonLimit = energonLimit == null ? null : energonLimit.trim();
    }

    public String getEnergonPrice() {
        return energonPrice;
    }

    public void setEnergonPrice(String energonPrice) {
        this.energonPrice = energonPrice == null ? null : energonPrice.trim();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId == null ? null : chainId.trim();
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType == null ? null : txType.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input == null ? null : input.trim();
    }
}