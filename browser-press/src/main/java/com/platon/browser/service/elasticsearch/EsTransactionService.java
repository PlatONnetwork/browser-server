package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.Transaction;
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

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class EsTransactionService extends EsService<Transaction>{
    @Autowired
    private EsTransactionRepository ESTransactionRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<Transaction> stage) throws IOException, InterruptedException {
        Set<Transaction> data = stage.getData();
        if(data.isEmpty()) return;
        int size = data.size()/POOL_SIZE;
        Set<Map<String, Transaction>> groups = new HashSet<>();
        try {
            Map<String,Transaction> group = new HashMap<>();
            for (Transaction e : data) {
                // 使用交易Hash作ES的docId
                group.put(e.getHash(),e);
                if(group.size()>=size){
                    groups.add(group);
                    group=new HashMap<>();
                }
            }
            if(group.size()>0) groups.add(group);

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, Transaction> g : groups) {
                try {
                    ESTransactionRepository.bulkAddOrUpdate(g);
                } finally {
                    latch.countDown();
                }
            }
            latch.await();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}
