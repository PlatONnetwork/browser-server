package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.queue.event.NodeEvent;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class StakeHandler extends AbstractHandler implements EventHandler<StakeEvent> {

    @Autowired
    private StakingMapper stakingMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    private List<Staking> stakingStage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( StakeEvent event, long sequence, boolean endOfBatch ) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            stakingStage.addAll(event.getStakingList());
            if(stakingStage.size()<batchSize) return;
            stakingMapper.batchInsert(stakingStage);
            long endTime = System.currentTimeMillis();
            printTps("质押",stakingStage.size(),startTime,endTime);
            stakingStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}