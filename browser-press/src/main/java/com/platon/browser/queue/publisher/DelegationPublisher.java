package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.queue.event.DelegationEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.DelegationHandler;
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
public class DelegationPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<DelegationEvent,List<Delegation>>
            TRANSLATOR = (event, sequence, delegationList)->{
        event.setDelegationList(delegationList);
    };
    @Value("${disruptor.queue.delegation.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<DelegationEvent> eventFactory = DelegationEvent::new;
    @Autowired
    private DelegationHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<DelegationEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Delegation> delegationList){
        ringBuffer.publishEvent(TRANSLATOR,delegationList);
    }
}
