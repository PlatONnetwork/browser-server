package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.DelegationReward;
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
public class EsDelegationRewardService extends EsService<DelegationReward>{

    @Autowired
    private EsDelegationRewardRepository ESDelegationRewardRepository;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void save(StageCache<DelegationReward> stage) throws IOException, InterruptedException {
        Set<DelegationReward> data = stage.getData();
        if(data.isEmpty()) return;
        int size = data.size()/POOL_SIZE;
        Set<Map<String,DelegationReward>> groups = new HashSet<>();
        try {
            Map<String,DelegationReward> group = new HashMap<>();
            for (DelegationReward b : data) {
                // 使用区块号作ES的docId
                group.put(b.getHash(), b);
                if(group.size()>=size){
                    groups.add(group);
                    group=new HashMap<>();
                }
            }
            if(group.size()>0) groups.add(group);

            CountDownLatch latch = new CountDownLatch(groups.size());
            for (Map<String, DelegationReward> g : groups) {
                try {
                	ESDelegationRewardRepository.bulkAddOrUpdate(g);
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
