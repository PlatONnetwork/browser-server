package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.OldErcTx;
import com.platon.browser.queue.handler.StageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class OldErc20TxService extends EsService<OldErcTx> {

    @Autowired
    private OldEsErc20TxRepository OldEsErc20TxRepository;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<OldErcTx> stage) throws IOException, InterruptedException {
        try {
            Set<OldErcTx> data = stage.getData();
            if (data.isEmpty()) {
                return;
            }
            int size = data.size() / POOL_SIZE;
            Set<Map<String, OldErcTx>> groups = new HashSet<>();
            Map<String, OldErcTx> group = new HashMap<>();
            for (OldErcTx record : data) {
                group.put(record.getHash(), record);
                if (group.size() >= size) {
                    groups.add(group);
                    group = new HashMap<>();
                }
            }
            if (group.size() > 0) groups.add(group);
            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, OldErcTx> g : groups) {
                try {
                    OldEsErc20TxRepository.bulkAddOrUpdate(g);
                } finally {
                    latch.countDown();
                }
            }
            latch.await();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}
