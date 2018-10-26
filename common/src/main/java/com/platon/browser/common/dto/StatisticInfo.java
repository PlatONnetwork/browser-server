package com.platon.browser.common.dto;

import com.platon.browser.common.dto.block.BlockStatistic;
import lombok.Data;

import java.util.List;

@Data
public class StatisticInfo {
    private int avgTime;

    private int current;

    private int maxTps;

    private int avgTransaction;

    private int dayTransaction;

    private List<BlockStatistic> blockStatisticList ;

}
