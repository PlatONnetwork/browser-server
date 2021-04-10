package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.queue.event.NodeOptEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.NodeOptHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 持久化事件生产者
 */
@Slf4j
@Component
public class NodeOptPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<NodeOptEvent, List<NodeOpt>> TRANSLATOR = (event, sequence, msg)->event.setNodeOptList(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private NodeOptHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.nodeopt.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return NodeOptEvent::new;
    }
}
