package com.platon.browser.dto.block;

import lombok.Data;

@Data
public class BlockInfo {
    private int height;

    private long timestamp;

    private long serverTime;

    private String node;

    private int transaction;

    private double blockReward;
}
