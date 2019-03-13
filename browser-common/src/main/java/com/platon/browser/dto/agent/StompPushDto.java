package com.platon.browser.dto.agent;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 11:39
 */
public class StompPushDto {

    //当前块高
    private long currentHeight;

    //当前出块节点
    private String miner;

    //共识节点数
    private int consensusCount;

    //当前区块交易数
    private long transactionCount;

    //地址数
    private long addressCount;

    //平均出块时间
    private BigDecimal avgTime;

    //当前tps
    private long current;

    //最大tps
    private long maxTps;

    //平均交易数
    private BigDecimal avgTransaction;

    //24小时内交易数
    private long dayTransaction;

    public long getCurrentHeight () {
        return currentHeight;
    }

    public void setCurrentHeight ( long currentHeight ) {
        this.currentHeight = currentHeight;
    }

    public String getMiner () {
        return miner;
    }

    public void setMiner ( String miner ) {
        this.miner = miner;
    }

    public int getConsensusCount () {
        return consensusCount;
    }

    public void setConsensusCount ( int consensusCount ) {
        this.consensusCount = consensusCount;
    }

    public long getTransactionCount () {
        return transactionCount;
    }

    public void setTransactionCount ( long transactionCount ) {
        this.transactionCount = transactionCount;
    }

    public long getAddressCount () {
        return addressCount;
    }

    public void setAddressCount ( long addressCount ) {
        this.addressCount = addressCount;
    }

    public BigDecimal getAvgTime () {
        return avgTime;
    }

    public void setAvgTime ( BigDecimal avgTime ) {
        this.avgTime = avgTime;
    }

    public long getCurrent () {
        return current;
    }

    public void setCurrent ( long current ) {
        this.current = current;
    }

    public long getMaxTps () {
        return maxTps;
    }

    public void setMaxTps ( long maxTps ) {
        this.maxTps = maxTps;
    }

    public BigDecimal getAvgTransaction () {
        return avgTransaction;
    }

    public void setAvgTransaction ( BigDecimal avgTransaction ) {
        this.avgTransaction = avgTransaction;
    }

    public long getDayTransaction () {
        return dayTransaction;
    }

    public void setDayTransaction ( long dayTransaction ) {
        this.dayTransaction = dayTransaction;
    }
}