package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.queue.event.DelegationEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.BlockHandler;
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
    private static final EventTranslatorOneArg<DelegationEvent,List<Delegation>> TRANSLATOR = (event, sequence, msg)->event.setDelegationList(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private DelegationHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.delegation.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return DelegationEvent::new;
    }
}
