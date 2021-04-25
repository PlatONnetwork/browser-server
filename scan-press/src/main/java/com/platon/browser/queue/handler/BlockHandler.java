package com.platon.browser.queue.handler;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.service.elasticsearch.EsBlockService;
import com.platon.browser.service.redis.RedisBlockService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockHandler extends AbstractHandler<BlockEvent> {

    @Autowired
    private EsBlockService esBlockService;

    @Autowired
    private RedisBlockService redisBlockService;

    @Setter
    @Getter
    @Value("${disruptor.queue.block.batch-size}")
    private volatile int batchSize;

    private StageCache<Block> stage = new StageCache<>();

    @PostConstruct
    private void init() {
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<Block> cache = stage.getData();
        try {
            cache.addAll(event.getBlockList());
            if (cache.size() < batchSize)
                return;
            esBlockService.save(stage);
            redisBlockService.save(stage.getData(), false);
            long endTime = System.currentTimeMillis();
            printTps("区块", cache.size(), startTime, endTime);
            cache.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

}