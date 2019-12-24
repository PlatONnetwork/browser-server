package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dao.mapper.RpPlanMapper;
import com.platon.browser.queue.event.RpPlanEvent;
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
public class RpPlanHandler extends AbstractHandler<RpPlanEvent> {

    @Autowired
    private RpPlanMapper rpPlanMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.rpplan.batch-size}")
    private volatile int batchSize;

    private List<RpPlan> stage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (RpPlanEvent event, long sequence, boolean endOfBatch ) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getRpPlanList());
            if(stage.size()<batchSize) return;
            rpPlanMapper.batchInsert(stage);
            long endTime = System.currentTimeMillis();
            printTps("锁仓",stage.size(),startTime,endTime);
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}