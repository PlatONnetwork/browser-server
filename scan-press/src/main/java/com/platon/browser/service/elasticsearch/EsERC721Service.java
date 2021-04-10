package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.ErcTx;
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

@Slf4j
@Service
public class EsERC721Service extends EsService<ErcTx> {

    @Autowired
    private EsErc721TxRepository esErc721TxRepository;

    @PostConstruct
    public void init() throws IOException {
        if (!esErc721TxRepository.existsIndex()) {
            Map<String, Object> setting = new HashMap(3);
            // 查询的返回数量，默认是10000
            setting.put("max_result_window", 2000000000);
            // 主碎片的数量
            setting.put("number_of_shards", 5);
            // 副本每个主碎片的数量
            setting.put("number_of_replicas", 1);
            esErc721TxRepository.createIndex(setting, null);
        }
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<ErcTx> stage) throws IOException, InterruptedException {
        Set<ErcTx> data = stage.getData();
        if (data.isEmpty()) {
            return;
        }
        int size = data.size() / POOL_SIZE;
        Set<Map<String, ErcTx>> groups = new HashSet<>();
        try {
            Map<String, ErcTx> group = new HashMap<>();
            for (ErcTx t : data) {
                group.put(generateUniqueDocId(t.getHash(), t.getFrom(), t.getTo(), t.getSeq()), t);
                if (group.size() >= size) {
                    groups.add(group);
                    group = new HashMap<>();
                }
            }
            if (group.size() > 0) {
                groups.add(group);
            }

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, ErcTx> g : groups) {
                try {
                    esErc721TxRepository.bulkAddOrUpdate(g);
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

    public String generateUniqueDocId(String txHash, String from, String to, long seq) {
        return seq + "_" + txHash.substring(0, txHash.length() / 2) + from.substring(0, from.length() / 2)
                + from.substring(0, to.length() / 2);
    }

}
