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
    private String energonLimit;
    private String energonUsed;
    private String blockReward;
    private String extraData;
    private long timeDiff;
    // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
}
