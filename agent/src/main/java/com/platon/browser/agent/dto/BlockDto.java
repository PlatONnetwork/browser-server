package com.platon.browser.agent.dto;

import javax.xml.crypto.Data;
import java.math.BigInteger;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 16:22
 */
public class BlockDto {

    /**
     * 区块高度
     */
    private Integer number;

    /**
     * 区块中交易列表
     */
    private List<TransactionDto> transaction;

    /**
     * 区块大小
     */
    private float size;

    /**
     * 区块hash
     */
    private String hash;

    /**
     * 能量消耗
     */
    private BigInteger energonUsed;

    /**
     * 平均能量消耗
     */
    private BigInteger energonAverage;

    /**
     * 能量限制
     */
    private BigInteger energonLimit;

    /**
     * 区块奖励
     */
    private String blockReward;

    /**
     * 父区块hash
     */
    private String parentHash;

    /**
     * 出块节点地址
     */
    private String miner;

    /**
     * 交易附加数据
     */
    private String extraData;

    /**
     *  Nonce值
     */
    private String nonce;

    private long timestamp;

    public long getTimestamp () {
        return timestamp;
    }

    public void setTimestamp ( long timestamp ) {
        this.timestamp = timestamp;
    }

    public Integer getNumber () {
        return number;
    }

    public void setNumber ( Integer number ) {
        this.number = number;
    }

    public List <TransactionDto> getTransaction () {
        return transaction;
    }

    public void setTransaction ( List <TransactionDto> transaction ) {
        this.transaction = transaction;
    }

    public float getSize () {
        return size;
    }

    public void setSize ( float size ) {
        this.size = size;
    }

    public String getHash () {
        return hash;
    }

    public void setHash ( String hash ) {
        this.hash = hash;
    }

    public BigInteger getEnergonUsed () {
        return energonUsed;
    }

    public void setEnergonUsed ( BigInteger energonUsed ) {
        this.energonUsed = energonUsed;
    }

    public BigInteger getEnergonAverage () {
        return energonAverage;
    }

    public void setEnergonAverage ( BigInteger energonAverage ) {
        this.energonAverage = energonAverage;
    }

    public BigInteger getEnergonLimit () {
        return energonLimit;
    }

    public void setEnergonLimit ( BigInteger energonLimit ) {
        this.energonLimit = energonLimit;
    }

    public String getBlockReward () {
        return blockReward;
    }

    public void setBlockReward ( String blockReward ) {
        this.blockReward = blockReward;
    }

    public String getParentHash () {
        return parentHash;
    }

    public void setParentHash ( String parentHash ) {
        this.parentHash = parentHash;
    }

    public String getMiner () {
        return miner;
    }

    public void setMiner ( String miner ) {
        this.miner = miner;
    }

    public String getExtraData () {
        return extraData;
    }

    public void setExtraData ( String extraData ) {
        this.extraData = extraData;
    }

    public String getNonce () {
        return nonce;
    }

    public void setNonce ( String nonce ) {
        this.nonce = nonce;
    }
}
