package com.platon.browser.service.elasticsearch;

import com.platon.browser.queue.handler.StageCache;
import lombok.Data;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Data
public abstract class EsService<T> {
    protected static final int POOL_SIZE = 8;
    protected ExecutorService THREAD_POOL = Executors.newFixedThreadPool(POOL_SIZE);
    abstract void save(StageCache<T> stage) throws IOException, InterruptedException;
}
