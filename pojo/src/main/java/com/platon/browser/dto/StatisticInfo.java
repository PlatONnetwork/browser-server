package com.platon.browser.dto;

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

}
