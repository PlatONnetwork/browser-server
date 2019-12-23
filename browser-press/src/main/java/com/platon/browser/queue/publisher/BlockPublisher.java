package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.BlockHandler;
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
public class BlockPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<BlockEvent,List<Block>>
    TRANSLATOR = (event, sequence, blockList)->{
        event.setBlockList(blockList);
    };
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<BlockEvent> eventFactory = BlockEvent::new;
    @Autowired
    private BlockHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<BlockEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Block> blockList){
        ringBuffer.publishEvent(TRANSLATOR,blockList);
    }
}
