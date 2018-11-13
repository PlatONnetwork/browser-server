package com.platon.browser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dto.cache.LimitQueue;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class StatisticInfo {
    private Long avgTime;

    private Long current;

    private Double maxTps;

    private BigDecimal avgTransaction;

    private Long dayTransaction;

    private StatisticGraphData graphData;

    @JsonIgnore
    private List<StatisticItem> blockStatisticList;
    @JsonIgnore
    private LimitQueue<StatisticItem> limitQueue;
    @JsonIgnore
    private Long transactionCount;
    @JsonIgnore
    private Long blockCount;
    @JsonIgnore
    private Long highestBlockNumber;
    @JsonIgnore
    private Long lowestBlockNumber;
    @JsonIgnore
    private Long highestBlockTimestamp;
    @JsonIgnore
    private Long lowestBlockTimestamp;

    @JsonIgnore
    private boolean changed=false;
    @JsonIgnore
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
}
