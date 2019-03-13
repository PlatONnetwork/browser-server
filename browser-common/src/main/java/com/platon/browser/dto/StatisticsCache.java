package com.platon.browser.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsCache {
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

    //出块节点名称
    private String nodeName;
    // 出块节点ID
    private String nodeId;
    //票价
    private BigDecimal ticketPrice;
    //占比
    private BigDecimal proportion;
    //当前票数
    private long voteCount;
}
