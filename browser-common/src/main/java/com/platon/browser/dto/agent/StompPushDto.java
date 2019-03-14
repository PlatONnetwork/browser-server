package com.platon.browser.dto.agent;

import lombok.Data;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 11:39
 */
@Data
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
}