package com.platon.browser.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticInfo {
    private BigDecimal avgTime;
    private long current;
    private long maxTps;
    private BigDecimal avgTransaction;
    private long dayTransaction;
    private StatisticGraphData graphData;
    private BigDecimal ticketPrice;
    private long voteAmount;
    private BigDecimal proportion;
}
