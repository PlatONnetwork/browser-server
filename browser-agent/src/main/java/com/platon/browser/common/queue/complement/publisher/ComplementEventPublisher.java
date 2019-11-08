package com.platon.browser.common.queue.complement.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.platon.browser.common.complement.param.BusinessParam;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
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
    private static final EventTranslatorThreeArg<ComplementEvent, CollectionEvent,List<ComplementNodeOpt>, List<BusinessParam>>
    TRANSLATOR = (event, sequence, collectionEvent,nodeOpts,businessParam)->event
            .setBlock(collectionEvent.getBlock())
            .setTransactions(collectionEvent.getTransactions())
            .setNodeOpts(nodeOpts)
            .setBusinessParams(businessParam);
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

    public void publish(CollectionEvent event, List<ComplementNodeOpt> nodeOpts,List<BusinessParam> businessParams) {
        ringBuffer.publishEvent(TRANSLATOR, event,nodeOpts,businessParams);
    }
}
