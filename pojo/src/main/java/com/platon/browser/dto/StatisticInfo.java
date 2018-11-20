package com.platon.browser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dto.cache.LimitQueue;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class StatisticInfo {
    private long avgTime;

    private long current;

    private long maxTps;

    private BigDecimal avgTransaction;

    private long dayTransaction;

    private StatisticGraphData graphData;

    @JsonIgnore
    private List<StatisticItem> blockStatisticList;
    @JsonIgnore
    private LimitQueue<StatisticItem> limitQueue;
    @JsonIgnore
    private long transactionCount;
    @JsonIgnore
    private long blockCount;
    @JsonIgnore
    private long highestBlockNumber;
    @JsonIgnore
    private long lowestBlockNumber;
    @JsonIgnore
    private long highestBlockTimestamp;
    @JsonIgnore
    private long lowestBlockTimestamp;

    @JsonIgnore
    private boolean changed=false;
    @JsonIgnore
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
}
