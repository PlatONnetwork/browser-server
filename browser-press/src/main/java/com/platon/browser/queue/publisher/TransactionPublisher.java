package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.TransactionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 持久化事件生产者
 */
@Slf4j
@Component
public class TransactionPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<TransactionEvent, List<Transaction>> TRANSLATOR = (event, sequence, msg)->event.setTransactionList(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private TransactionHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.transaction.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return TransactionEvent::new;
    }
}
