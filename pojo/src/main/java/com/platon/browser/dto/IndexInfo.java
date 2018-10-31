package com.platon.browser.dto;

import lombok.Data;

/**
 * 监控指标
 */
@Data
public class IndexInfo {
    private long currentHeight;
    private String node;
    private long currentTransaction;
    private long consensusNodeAmount;
    private long addressAmount;
    private int voteAmount;
    private double proportion;
    private double ticketPrice;
}
