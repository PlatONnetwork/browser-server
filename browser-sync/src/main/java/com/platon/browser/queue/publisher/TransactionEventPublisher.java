package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.AbstractPublisher;
import com.platon.browser.queue.handler.TransactionEventHandler;
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
public class TransactionEventPublisher extends AbstractPublisher<List<Transaction>> {
    private static final EventTranslatorOneArg<List<Transaction>, List <Transaction>>
    TRANSLATOR = (event, sequence, transactions)->event.addAll(transactions);
    @Value("${disruptor.queue.transaction.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }

    private EventFactory<List<Transaction>> eventFactory = ArrayList::new;
    @Autowired
    private TransactionEventHandler handler;

    @Getter
    private Disruptor<List<Transaction>> disruptor;

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
