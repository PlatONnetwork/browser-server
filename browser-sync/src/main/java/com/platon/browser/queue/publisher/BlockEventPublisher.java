package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.AbstractPublisher;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.queue.handler.BlockEventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * 自检事件生产者
 */
@Slf4j
@Component
public class BlockEventPublisher extends AbstractPublisher<BlockEvent> {
    private static final EventTranslatorOneArg<BlockEvent,CompletableFuture<Block>>
    TRANSLATOR = (event, sequence, blockCF)->event.setBlockCF(blockCF);
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }

    private EventFactory<BlockEvent> eventFactory = () -> BlockEvent.builder().build();
    @Autowired
    private BlockEventHandler handler;

    @Getter
    private Disruptor<BlockEvent> disruptor;

    @PostConstruct
    public void init(){
        disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(BlockEventPublisher.class.getSimpleName(),this);
    }

    public void publish(CompletableFuture<Block> blockCF){
        ringBuffer.publishEvent(TRANSLATOR, blockCF);
    }
}
