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
    private long nodeOptMaxId;
    private long addressCount;
    private long lastBlockNumber;
}
