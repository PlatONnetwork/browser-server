package com.platon.browser.service.elasticsearch;

import com.platon.browser.dao.entity.Delegation;
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
public class EsDelegationService extends EsService<Delegation> {

    @Autowired
    private EsDelegationRepository esDelegationRepository;

    @PostConstruct
    public void init() throws IOException {
        if (!esDelegationRepository.existsIndex()) {
            Map<String, Object> setting = new HashMap(3);
            // 查询的返回数量，默认是10000
            setting.put("max_result_window", 2000000000);
            // 主碎片的数量
            setting.put("number_of_shards", 5);
            // 副本每个主碎片的数量
            setting.put("number_of_replicas", 1);
            esDelegationRepository.createIndex(setting, null);
        }
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<Delegation> stage) throws IOException, InterruptedException {
        Set<Delegation> data = stage.getData();
        if (data.isEmpty()) {
            return;
        }
        int size = data.size() / POOL_SIZE;
        Set<Map<String, Delegation>> groups = new HashSet<>();
        try {
            Map<String, Delegation> group = new HashMap<>();
            for (Delegation e : data) {
                // 使用(<节点ID>-<质押区块号>-<委托人地址>)作ES的docId
                group.put(e.getNodeId() + "-" + e.getStakingBlockNum() + "-" + e.getDelegateAddr(), e);
                if (group.size() >= size) {
                    groups.add(group);
                    group = new HashMap<>();
                }
            }
            if (group.size() > 0) {
                groups.add(group);
            }

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, Delegation> g : groups) {
                try {
                    esDelegationRepository.bulkAddOrUpdate(g);
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
