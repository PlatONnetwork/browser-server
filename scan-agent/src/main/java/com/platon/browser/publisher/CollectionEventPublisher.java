package com.platon.browser.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.handler.CollectionEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
    @Override
    public int getRingBufferSize() {
        return config.getCollectionBufferSize();
    }
    private EventFactory<CollectionEvent> eventFactory = CollectionEvent::new;
    @Resource
    private CollectionEventHandler collectionEventHandler;

    @PostConstruct
    private void init(){
        Disruptor<CollectionEvent> disruptor = new Disruptor<>(eventFactory, getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(collectionEventHandler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(CollectionEventPublisher.class.getSimpleName(),this);
    }

    public void publish(Block block, List<Transaction> transactions,EpochMessage epochMessage){
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,epochMessage);
    }
}
