package com.platon.browser.utils;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CounterBean {

    private long blockCount;

    private long transactionCount;

    private long nodeOptCount;

    private long nodeCount;

    private long stakingCount;

    private long delegationCount;

    private long proposalCount;

    private long voteCount;

    private long rpplanCount;

    private long slashCount;

    private long nodeOptMaxId;

    private long addressCount;

    private long lastBlockNumber;

    private long rewardCount;

    private long estimateCount;

    private long tokenCount;

    private long tokenHolderCount;

    private long tokenInventoryCount;

    private long tokenTransferCount;

    /**
     * 耗时
     */
    private String time;

}
