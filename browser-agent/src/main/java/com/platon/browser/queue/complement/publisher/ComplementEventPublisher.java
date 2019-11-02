package com.platon.browser.queue.complement.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.queue.complement.event.ComplementEvent;
import com.platon.browser.queue.complement.handler.IComplementEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * 补充事件发布者
 */
@Slf4j
@Component
public class ComplementEventPublisher {
    private static final EventTranslatorThreeArg<ComplementEvent, CollectionBlock, List<CollectionTransaction>, List<BusinessParam>>
    TRANSLATOR = (event, sequence, block,transactions,businessParam)->event.setBlock(block).setTransactions(transactions).setBusinessParams(businessParam);
    private RingBuffer<ComplementEvent> ringBuffer;
    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.complement.buffer-size}")
    private int ringBufferSize;
    private EventFactory<ComplementEvent> eventFactory = () -> ComplementEvent.builder().build();
    @Autowired
    private IComplementEventHandler handler;
    // 事件处理线程生产工厂
    ThreadFactory consumeThreadFactory = Thread::new;

    @PostConstruct
    private void init(){
        Disruptor<ComplementEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, consumeThreadFactory);
        // 设置事件处理器
        disruptor.handleEventsWith(handler);
        // 启动Disruptor,让所有生产和消费线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(CollectionBlock block, List<CollectionTransaction> transactions,List<BusinessParam> businessParams){
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,businessParams);
    }
}
