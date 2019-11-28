package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.AbstractPublisher;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.handler.TransactionEventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 自检事件生产者
 */
@Slf4j
@Component
public class TransactionEventPublisher extends AbstractPublisher<TransactionEvent> {
    private static final EventTranslatorOneArg<TransactionEvent, List <Transaction>>
    TRANSLATOR = (event, sequence, transactions)->event.setTransactions(transactions);
    @Value("${disruptor.queue.transaction.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }

    private EventFactory<TransactionEvent> eventFactory = () -> TransactionEvent.builder().build();
    @Autowired
    private TransactionEventHandler handler;

    @Getter
    private Disruptor<TransactionEvent> disruptor;

    @PostConstruct
    public void init(){
        disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(TransactionEventPublisher.class.getSimpleName(),this);
    }

    public void publish(List<Transaction> transactions){
        ringBuffer.publishEvent(TRANSLATOR, transactions);
    }
}
