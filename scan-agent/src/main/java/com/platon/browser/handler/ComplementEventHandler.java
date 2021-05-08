package com.platon.browser.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.bean.ComplementEvent;
import com.platon.browser.publisher.PersistenceEventPublisher;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class ComplementEventHandler implements EventHandler<ComplementEvent> {

    @Resource
    private PersistenceEventPublisher persistenceEventPublisher;

    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        surroundExec(event, sequence, endOfBatch);
    }

    private void surroundExec(ComplementEvent event, long sequence, boolean endOfBatch) {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(ComplementEvent event, long sequence, boolean endOfBatch) {
        try {
            // 发布至持久化队列
            persistenceEventPublisher.publish(event.getBlock(), event.getTransactions(), event.getNodeOpts(), event.getDelegationRewards(), event.getTraceId());
            // 释放对象引用
            event.releaseRef();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

}