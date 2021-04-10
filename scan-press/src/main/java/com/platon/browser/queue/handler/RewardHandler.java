package com.platon.browser.queue.handler;

import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.queue.event.RewardEvent;
import com.platon.browser.service.elasticsearch.EsDelegationRewardService;

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

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class RewardHandler extends AbstractHandler<RewardEvent> {

    @Autowired
    private EsDelegationRewardService delegationRewardService;

    @Setter
    @Getter
    @Value("${disruptor.queue.reward.batch-size}")
    private volatile int batchSize;

    private StageCache<DelegationReward> stage = new StageCache<>();
    @PostConstruct
    private void init(){
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (RewardEvent event, long sequence, boolean endOfBatch ) throws IOException, InterruptedException{
        long startTime = System.currentTimeMillis();
        Set<DelegationReward> cache = stage.getData();
        try {
        	cache.addAll(event.getDelegationRewards());
            if(cache.size()<batchSize) return;
            delegationRewardService.save(stage);
            long endTime = System.currentTimeMillis();
            printTps("委托奖励",cache.size(),startTime,endTime);
            cache.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}