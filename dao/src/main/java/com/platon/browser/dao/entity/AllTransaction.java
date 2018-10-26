package com.platon.browser.dao.entity;

import java.util.Date;

public class AllTransaction {
    private String chainId;

    private String txHash;

    private Date blockTime;

    private String from;

    private String to;

    private String value;

    private Integer energonUsed;

    private Integer energonLimit;

    private String energonPrice;

    private Long transactionIndex;

    private String actualTxCost;

    private Long txReceiptStatus;

    private String txType;

    private Date serverTime;

    private Date createTime;

    private Date updateTime;

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId == null ? null : chainId.trim();
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Integer getEnergonUsed() {
        return energonUsed;
    }

    public void setEnergonUsed(Integer energonUsed) {
        this.energonUsed = energonUsed;
    }

    public Integer getEnergonLimit() {
        return energonLimit;
    }

    public void setEnergonLimit(Integer energonLimit) {
        this.energonLimit = energonLimit;
    }

    public String getEnergonPrice() {
        return energonPrice;
    }

    public void setEnergonPrice(String energonPrice) {
        this.energonPrice = energonPrice == null ? null : energonPrice.trim();
    }

    public Long getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(Long transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getActualTxCost() {
        return actualTxCost;
    }

    public void setActualTxCost(String actualTxCost) {
        this.actualTxCost = actualTxCost == null ? null : actualTxCost.trim();
    }

    public Long getTxReceiptStatus() {
        return txReceiptStatus;
    }

    public void setTxReceiptStatus(Long txReceiptStatus) {
        this.txReceiptStatus = txReceiptStatus;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType == null ? null : txType.trim();
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
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
}