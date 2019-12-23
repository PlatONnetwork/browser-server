package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.queue.event.NodeOptEvent;
import com.platon.browser.queue.handler.NodeOptHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 持久化事件生产者
 */
@Slf4j
@Component
public class NodeOptPublisher {
    private static final EventTranslatorOneArg<NodeOptEvent,List<NodeOpt>>
    TRANSLATOR = (event, sequence, nodeOptList)->{
        event.setNodeOptList(nodeOptList);
    };
    @Value("${disruptor.queue.nodeopt.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<NodeOptEvent> eventFactory = NodeOptEvent::new;
    @Autowired
    private NodeOptHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<NodeOptEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<NodeOpt> nodeOptList){
        ringBuffer.publishEvent(TRANSLATOR,nodeOptList);
    }
}
