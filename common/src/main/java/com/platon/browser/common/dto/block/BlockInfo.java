package com.platon.browser.common.dto.block;

import lombok.Data;

@Data
public class BlockInfo {
    private int height;

    private long timeStamp;

    private long serverTime;

    private String node;

    private int transaction;

    private double blockReward;
}
