package com.platon.browser.common.queue.gasestimate.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.common.queue.gasestimate.event.GasEstimateEvent;
import org.springframework.retry.annotation.Retryable;


/**
 * Gas price 估算处理器接口
 */
public interface IGasEstimateEventHandler extends EventHandler<GasEstimateEvent> {

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    void onEvent(GasEstimateEvent event, long sequence, boolean endOfBatch) throws Exception;
}