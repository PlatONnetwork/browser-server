package com.platon.browser.common.dto;

import lombok.Data;

/**
 * 监控指标
 */
@Data
public class Index {
    private int currentHeight;
    private String node;
    private int currentTransaction;
    private int consensusNodeAmount;
    private int addressAmount;
    private int voteAmount;
    private double proportion;
    private double ticketPrice;
}
