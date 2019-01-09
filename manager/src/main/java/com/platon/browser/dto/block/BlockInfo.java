package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockInfo {
    private long height;

    private long timestamp;

    private long serverTime;

    private String node;

    private long transaction;

    private String blockReward;
}
