package com.platon.browser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 监控指标
 */
@Data
public class IndexInfo {
    private long currentHeight;
    private String miner;
    private String nodeName;
    private long currentTransaction;
    private long consensusNodeAmount;
    private long addressAmount;
    private int voteAmount;
    private double proportion;
    private double ticketPrice;
    @JsonIgnore
    private boolean changed=false; // 是否有改动
    @JsonIgnore
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); // 读写锁
}
