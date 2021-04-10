package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.queue.event.RpPlanEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.RpPlanHandler;
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
public class RpPlanPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<RpPlanEvent, List<RpPlan>> TRANSLATOR = (event, sequence, msg)->event.setRpPlanList(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private RpPlanHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.rpplan.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return RpPlanEvent::new;
    }
}
