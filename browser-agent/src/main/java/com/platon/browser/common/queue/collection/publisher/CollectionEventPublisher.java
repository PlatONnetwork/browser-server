package com.platon.browser.common.queue.collection.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.common.collection.dto.EpochMessage;
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
import java.util.concurrent.ThreadFactory;

/**
 * 区块搜集事件发布者
 */
@Slf4j
@Component
public class CollectionEventPublisher {
    private static final EventTranslatorThreeArg<CollectionEvent, Block, List<Transaction>,EpochMessage>
    TRANSLATOR = (event, sequence, block,transactions,epochMessage)->event.setBlock(block).setTransactions(transactions).setEpochMessage(epochMessage);
    private RingBuffer<CollectionEvent> ringBuffer;
    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.collection.buffer-size}")
    private int ringBufferSize;
    private EventFactory<CollectionEvent> eventFactory = () -> CollectionEvent.builder().build();
    @Autowired
    private ICollectionEventHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<CollectionEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        // 设置事件处理器
        disruptor.handleEventsWith(handler);
        // 启动Disruptor,让所有生产和消费线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(Block block, List<Transaction> transactions,EpochMessage epochMessage){
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,epochMessage);
    }
}
