package com.platon.browser.dto;

import com.platon.browser.dao.entity.StatisticTransactionView;
import com.platon.browser.dto.block.BlockStatistic;
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

    private List<StatisticTransactionView> blockStatisticList ;

}
