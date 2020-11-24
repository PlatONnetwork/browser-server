package com.platon.browser.queue.handler;

import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.queue.event.ESTokenTransferRecordEvent;
import com.platon.browser.service.elasticsearch.EsTokenTransferRecordService;
import com.platon.browser.service.redis.RedisTransferTokenRecordService;
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

@Slf4j
@Component
public class ESTokenTransferRecordHandler extends AbstractHandler<ESTokenTransferRecordEvent> {

    @Autowired
    private EsTokenTransferRecordService esTokenTransferRecordService;

    @Autowired
    private RedisTransferTokenRecordService redisTransferTokenRecordService;

    @Setter
    @Getter
    @Value("${disruptor.queue.token-transfer.batch-size}")
    private volatile int batchSize;

    private StageCache<ESTokenTransferRecord> stage = new StageCache<>();

    @PostConstruct
    private void init() {
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(ESTokenTransferRecordEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<ESTokenTransferRecord> cache = stage.getData();
        try {
            cache.addAll(event.getEsTokenTransferRecordList());
            if (cache.size() < batchSize) {
                return;
            }
            esTokenTransferRecordService.save(stage);
            redisTransferTokenRecordService.save(cache, false);
            long endTime = System.currentTimeMillis();
            printTps("Token交易", cache.size(), startTime, endTime);
            cache.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}