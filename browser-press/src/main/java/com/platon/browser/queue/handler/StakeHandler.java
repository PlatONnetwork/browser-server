package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.queue.event.StakeEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class StakeHandler extends AbstractHandler<StakeEvent> {

    @Autowired
    private StakingMapper stakingMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.stake.batch-size}")
    private volatile int batchSize;

    private List<Staking> stage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( StakeEvent event, long sequence, boolean endOfBatch ) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getStakingList());
            if(stage.size()<batchSize) return;
            stakingMapper.batchInsert(stage);
            long endTime = System.currentTimeMillis();
            printTps("质押",stage.size(),startTime,endTime);
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}