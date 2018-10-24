package com.platon.browser.dao.entity;

import java.util.Date;

public class Transaction {
    private String hash;

    private Long blockNumber;

    private String blockHash;

    private String from;

    private String to;

    private Date timestamp;

    private Integer energonUsed;

    private Integer energonLimit;

    private String energonPrice;

    private String value;

    private String nonce;

    private Integer transactionIndex;

    private Integer txReceiptStatus;

    private String actualTxCost;

    private String txType;

    private String chainId;

    private Date createTime;

    private Date updateTime;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash == null ? null : blockHash.trim();
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce == null ? null : nonce.trim();
    }

    public Integer getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(Integer transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public Integer getTxReceiptStatus() {
        return txReceiptStatus;
    }

    public void setTxReceiptStatus(Integer txReceiptStatus) {
        this.txReceiptStatus = txReceiptStatus;
    }

    public String getActualTxCost() {
        return actualTxCost;
    }

    public void setActualTxCost(String actualTxCost) {
        this.actualTxCost = actualTxCost == null ? null : actualTxCost.trim();
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType == null ? null : txType.trim();
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId == null ? null : chainId.trim();
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