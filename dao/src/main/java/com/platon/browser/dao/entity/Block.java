package com.platon.browser.dao.entity;

import java.util.Date;

public class Block {
    private String hash;

    private Long number;

    private String parentHash;

    private String nonce;

    private String miner;

    private String extraData;

    private Integer size;

    private Integer energonUsed;

    private Integer energonLimit;

    private Integer energonAverage;

    private Date timestamp;

    private String blockReward;

    private String chainId;

    private Date createTime;

    private Date updateTime;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash == null ? null : parentHash.trim();
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce == null ? null : nonce.trim();
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner == null ? null : miner.trim();
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData == null ? null : extraData.trim();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public Integer getEnergonAverage() {
        return energonAverage;
    }

    public void setEnergonAverage(Integer energonAverage) {
        this.energonAverage = energonAverage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(String blockReward) {
        this.blockReward = blockReward == null ? null : blockReward.trim();
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