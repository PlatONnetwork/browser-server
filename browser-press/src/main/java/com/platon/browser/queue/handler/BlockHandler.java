package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockHandler extends AbstractHandler<BlockEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Setter
    @Getter
    @Value("${disruptor.queue.block.batch-size}")
    private volatile int batchSize;

    private Set<Block> stage = new HashSet<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getBlockList());
            if(stage.size()<batchSize) return;
            esImportService.batchImport(stage, Collections.emptySet(),Collections.emptySet());
            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            redisImportService.batchImport(stage,Collections.emptySet(),statistics);
            long endTime = System.currentTimeMillis();
            printTps("区块",stage.size(),startTime,endTime);
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}