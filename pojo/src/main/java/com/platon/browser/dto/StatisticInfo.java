package com.platon.browser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StatisticInfo {
    private long avgTime;

    private long current;

    private double maxTps;

    private BigDecimal avgTransaction;

    private long dayTransaction;

    private List<StatisticItem> blockStatisticList ;

    @JsonIgnore
    private long transactionCount;
    @JsonIgnore
    private long blockCount;
    @JsonIgnore
    private long highestBlockNumber;
    @JsonIgnore
    private long lowestBlockNumber;

}
