package com.platon.browser.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class Statistic {
    private int avgTime;

    private int current;

    private int maxTps;

    private int avgTransaction;

    private int dayTransaction;

    private List<BlockStatistic> blockStatisticList ;

}
