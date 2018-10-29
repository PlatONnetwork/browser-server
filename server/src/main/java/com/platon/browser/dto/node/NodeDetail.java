package com.platon.browser.dto.node;

import lombok.Data;

@Data
public class NodeDetail {
    private int height;

    private long timestamp;

    private int transaction;

    private int size;

    private String miner;

    private int energonUsed;

    private int energonAverage;

    private double blockReward;

    private long serverTime;
}