package com.platon.browser.queue.handler;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class StageCache<T> {
    private Set<T> data = new HashSet<>();
    private long batchSize;
}
