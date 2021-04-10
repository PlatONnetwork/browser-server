package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.handler.StageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class EsTransactionService extends EsService<Transaction> {

    @Autowired
    private EsTransactionRepository esTransactionRepository;

    @PostConstruct
    public void init() throws IOException {
        if (!esTransactionRepository.existsIndex()) {
            Map<String, Object> setting = new HashMap(3);
            // 查询的返回数量，默认是10000
            setting.put("max_result_window", 2000000000);
            // 主碎片的数量
            setting.put("number_of_shards", 5);
            // 副本每个主碎片的数量
            setting.put("number_of_replicas", 1);
            esTransactionRepository.createIndex(setting, null);
        }
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<Transaction> stage) throws IOException, InterruptedException {
        Set<Transaction> data = stage.getData();
        if (data.isEmpty()) {
            return;
        }
        int size = data.size() / POOL_SIZE;
        Set<Map<String, Transaction>> groups = new HashSet<>();
        try {
            Map<String, Transaction> group = new HashMap<>();
            for (Transaction e : data) {
                // 使用交易Hash作ES的docId
                group.put(e.getHash(), e);
                if (group.size() >= size) {
                    groups.add(group);
                    group = new HashMap<>();
                }
            }
            if (group.size() > 0) {
                groups.add(group);
            }

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, Transaction> g : groups) {
                try {
                    esTransactionRepository.bulkAddOrUpdate(g);
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
