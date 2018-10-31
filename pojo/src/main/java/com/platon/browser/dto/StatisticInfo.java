package com.platon.browser.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StatisticInfo {
    private long avgTime;

    private int current;

    private int maxTps;

    private BigDecimal avgTransaction;

    private int dayTransaction;

    private List<StatisticItem> blockStatisticList ;

}
