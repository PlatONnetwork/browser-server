package com.platon.browser.persistence.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.common.queue.AbstractPublisher;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.persistence.queue.event.PersistenceEvent;
import com.platon.browser.persistence.queue.handler.PersistenceEventHandler;
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
public class PersistenceEventPublisher extends AbstractPublisher<PersistenceEvent> {
    private static final EventTranslatorThreeArg<PersistenceEvent, Block, List<Transaction>,List<NodeOpt>>
    TRANSLATOR = (event, sequence, block,transactions,nodeOpts)->{
        event.setBlock(block);
        event.setTransactions(transactions);
        event.setNodeOpts(nodeOpts);
    };
    @Value("${disruptor.queue.persistence.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }
    private EventFactory<PersistenceEvent> eventFactory = PersistenceEvent::new;
    @Autowired
    private PersistenceEventHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<PersistenceEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(PersistenceEventPublisher.class.getSimpleName(),this);
    }

    public void publish(Block block, List<Transaction> transactions, List<NodeOpt> nodeOpts){
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,nodeOpts);
    }
}
