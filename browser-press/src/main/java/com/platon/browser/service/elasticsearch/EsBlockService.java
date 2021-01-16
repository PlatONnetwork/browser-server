package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.handler.StageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class EsBlockService extends EsService<Block>{

    @Autowired
    private EsBlockRepository ESBlockRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<Block> stage) throws IOException, InterruptedException {
        Set<Block> data = stage.getData();
        if(data.isEmpty()) return;
        int size = data.size()/POOL_SIZE;
        Set<Map<String,Block>> groups = new HashSet<>();
        try {
            Map<String,Block> group = new HashMap<>();
            for (Block b : data) {
                // 使用区块号作ES的docId
                group.put(b.getNum().toString(), b);
                if(group.size()>=size){
                    groups.add(group);
                    group=new HashMap<>();
                }
            }
            if(group.size()>0) groups.add(group);

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, Block> g : groups) {
                try {
                    ESBlockRepository.bulkAddOrUpdate(g);
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
