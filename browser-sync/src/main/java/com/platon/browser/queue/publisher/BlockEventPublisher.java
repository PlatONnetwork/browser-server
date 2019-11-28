package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.AbstractPublisher;
import com.platon.browser.queue.handler.BlockEventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 自检事件生产者
 */
@Slf4j
@Component
public class BlockEventPublisher extends AbstractPublisher<List<Block>> {
    private static final EventTranslatorOneArg<List<Block>,List<Block>>
    TRANSLATOR = (event, sequence, blocks)->event.addAll(blocks);
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }

    private EventFactory<List<Block>> eventFactory = ArrayList::new;
    @Autowired
    private BlockEventHandler handler;

    @Getter
    private Disruptor<List<Block>> disruptor;

    @PostConstruct
    public void init(){
        disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(BlockEventPublisher.class.getSimpleName(),this);
    }

    public void publish(List<Block> blocks){
        ringBuffer.publishEvent(TRANSLATOR, blocks);
    }
}
