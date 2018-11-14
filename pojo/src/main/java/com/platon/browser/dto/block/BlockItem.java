package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockItem {
    private long height;
    private long timestamp;
    private long transaction;
    private int size;
    private String miner;
    private String energonUsed;
    private String energonLimit;
    private String energonAverage;
    private String blockReward;
    private long serverTime;
}
