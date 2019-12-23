package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.mapper.NodeMapper;
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

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class StakeHandler extends AbstractHandler implements EventHandler<StakeEvent> {

    @Autowired
    private NodeMapper nodeMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( StakeEvent event, long sequence, boolean endOfBatch ) throws Exception {

    }
}