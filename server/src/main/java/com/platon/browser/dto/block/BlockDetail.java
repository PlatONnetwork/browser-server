package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockDetail {
    private long height;
    private long timestamp;
    private long transaction;
    private String hash;
    private String parentHash;
    private String miner;
    private int size;
    private int energonLimit;
    private int energonUsed;
    private String blockReward;
    private String extraData;
}
