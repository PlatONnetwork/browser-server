package com.platon.browser.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.bean.GasEstimateEvent;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.handler.GasEstimateEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Gas price 估算事件发布者
 */
@Slf4j
@Component
public class GasEstimateEventPublisher extends AbstractPublisher<GasEstimateEvent> {

    private static final EventTranslatorVararg<GasEstimateEvent>
            TRANSLATOR = (event, sequence, args) -> {
        event.setSeq((Long) args[0]);
        event.setEstimateList((List<GasEstimate>) args[1]);
        event.setTraceId((String) args[2]);
    };

    @Override
    public int getRingBufferSize() {
        return config.getGasEstimateBufferSize();
    }

    private EventFactory<GasEstimateEvent> eventFactory = GasEstimateEvent::new;

    @Resource
    private GasEstimateEventHandler gasEstimateEventHandler;

    @PostConstruct
    private void init() {
        Disruptor<GasEstimateEvent> disruptor = new Disruptor<>(eventFactory, getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(gasEstimateEventHandler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(GasEstimateEventPublisher.class.getSimpleName(), this);
    }

    public void publish(Long seq, List<GasEstimate> estimateList, String traceId) {
        ringBuffer.publishEvent(TRANSLATOR, seq, estimateList, traceId);
    }

}
