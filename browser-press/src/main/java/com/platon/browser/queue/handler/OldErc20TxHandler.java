package com.platon.browser.queue.handler;

import com.platon.browser.elasticsearch.dto.OldErcTx;
import com.platon.browser.queue.event.ESTokenTransferRecordEvent;
import com.platon.browser.service.elasticsearch.OldErc20TxService;
import com.platon.browser.service.redis.OldRedisErc20TxService;
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
public class OldErc20TxHandler extends AbstractHandler<ESTokenTransferRecordEvent> {

    @Autowired
    private OldErc20TxService oldErc20TxService;

    @Autowired
    private OldRedisErc20TxService oldRedisErc20TxService;

    @Setter
    @Getter
    @Value("${disruptor.queue.token-transfer.batch-size}")
    private volatile int batchSize;

    private StageCache<OldErcTx> stage = new StageCache<>();

    @PostConstruct
    private void init() {
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(ESTokenTransferRecordEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<OldErcTx> cache = stage.getData();
        try {
            cache.addAll(event.getOldErcTxList());
            if (cache.size() < batchSize) {
                return;
            }
            oldErc20TxService.save(stage);
            oldRedisErc20TxService.save(cache, false);
            long endTime = System.currentTimeMillis();
            printTps("Token交易", cache.size(), startTime, endTime);
            cache.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}