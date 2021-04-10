package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.queue.event.DelegationEvent;
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
public class DelegationHandler extends AbstractHandler<DelegationEvent> {

    @Autowired
    private DelegationMapper delegationMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.delegation.batch-size}")
    private volatile int batchSize;

    private List<Delegation> stage = new ArrayList<>();

    @PostConstruct
    private void init() {
        this.setLogger(log);
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(DelegationEvent event, long sequence, boolean endOfBatch) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getDelegationList());
            if (stage.size() < batchSize) {
                return;
            }
            delegationMapper.batchInsert(stage);
            long endTime = System.currentTimeMillis();
            printTps("委托", stage.size(), startTime, endTime);
            stage.clear();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

}