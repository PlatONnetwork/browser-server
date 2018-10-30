package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockList {
    private long height;
    private long timestamp;
    private int transaction;
    private int size;
    private String miner;
    private int energonUsed;
    private int energonLimit;
    private int energonAverage;
    private String blockReward;
    private long serverTime;
}
