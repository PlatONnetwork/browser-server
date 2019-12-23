package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.TransactionHandler;
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
public class TransactionPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<TransactionEvent,List<Transaction>>
    TRANSLATOR = (event, sequence, transactionList)->{
        event.setTransactionList(transactionList);
    };
    @Value("${disruptor.queue.transaction.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<TransactionEvent> eventFactory = TransactionEvent::new;
    @Autowired
    private TransactionHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<TransactionEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Transaction> transactionList){
        ringBuffer.publishEvent(TRANSLATOR,transactionList);
    }
}
