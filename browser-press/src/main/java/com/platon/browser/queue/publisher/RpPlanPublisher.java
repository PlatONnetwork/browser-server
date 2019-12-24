package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.queue.event.RpPlanEvent;
import com.platon.browser.queue.event.SlashEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.RpPlanHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class RpPlanPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<RpPlanEvent,List<RpPlan>>
            TRANSLATOR = (event, sequence, rpPlanList)->{
        event.setRpPlanList(rpPlanList);
    };
    @Value("${disruptor.queue.rpplan.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<RpPlanEvent> eventFactory = RpPlanEvent::new;
    @Autowired
    private RpPlanHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<RpPlanEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<RpPlan> rpPlanList){
        ringBuffer.publishEvent(TRANSLATOR,rpPlanList);
    }
}
