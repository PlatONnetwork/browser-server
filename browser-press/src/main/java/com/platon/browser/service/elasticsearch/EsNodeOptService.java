package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.NodeOpt;
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
public class EsNodeOptService extends EsService<NodeOpt>{
    @Autowired
    private EsNodeOptRepository ESNodeOptRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<NodeOpt> stage) throws IOException, InterruptedException {
        Set<NodeOpt> data = stage.getData();
        if(data.isEmpty()) return;
        int size = data.size()/POOL_SIZE;
        Set<Map<String, NodeOpt>> groups = new HashSet<>();
        try {
            Map<String,NodeOpt> group = new HashMap<>();
            for (NodeOpt e : data) {
                // 使用(<id>)作ES的docId
                group.put(e.getId().toString(),e);
                if(group.size()>=size){
                    groups.add(group);
                    group=new HashMap<>();
                }
            }
            if(group.size()>0) groups.add(group);

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, NodeOpt> g : groups) {
                try {
                    ESNodeOptRepository.bulkAddOrUpdate(g);
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
