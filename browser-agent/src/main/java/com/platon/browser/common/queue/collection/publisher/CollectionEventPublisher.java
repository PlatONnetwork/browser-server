package com.platon.browser.common.queue.collection.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.queue.AbstractPublisher;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 区块搜集事件发布者
 */
@Slf4j
@Component
public class CollectionEventPublisher extends AbstractPublisher<CollectionEvent> {
    private static final EventTranslatorThreeArg<CollectionEvent, Block, List<Transaction>,EpochMessage>
    TRANSLATOR = (event, sequence, block,transactions,epochMessage)->{
        event.setBlock(block);
        event.setTransactions(transactions);
        event.setEpochMessage(epochMessage);
    };
    @Value("${disruptor.queue.collection.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }
    private EventFactory<CollectionEvent> eventFactory = CollectionEvent::new;
    @Autowired
    private ICollectionEventHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<CollectionEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(CollectionEventPublisher.class.getSimpleName(),this);
    }

    public void publish(Block block, List<Transaction> transactions,EpochMessage epochMessage){
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,epochMessage);
    }
}
