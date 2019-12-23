package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.queue.event.NodeEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.NodeHandler;
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
public class NodePublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<NodeEvent,List<Node>>
            TRANSLATOR = (event, sequence, nodeList)->{
        event.setNodeList(nodeList);
    };
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<NodeEvent> eventFactory = NodeEvent::new;
    @Autowired
    private NodeHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<NodeEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Node> nodeList){
        ringBuffer.publishEvent(TRANSLATOR,nodeList);
    }
}
