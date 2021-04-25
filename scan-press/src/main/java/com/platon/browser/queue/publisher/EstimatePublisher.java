package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.queue.event.EstimateEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.EstimateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class EstimatePublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<EstimateEvent, List<GasEstimate>> TRANSLATOR = (event, sequence, msg)->event.setGasEstimates(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private EstimateHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.estimate.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return EstimateEvent::new;
    }
}
