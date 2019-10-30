package com.platon.browser.application;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.complement.queue.handler.CollectionBlockEventHandler;
import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import com.platon.browser.queue.event.collection.CollectionBlockEventBody;
import com.platon.browser.queue.event.collection.CollectionBlockEventFactory;
import com.platon.browser.collection.producer.CollectionBlockEventProducerWithTranslator;
import com.platon.browser.queue.callback.ConsumeCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;

/**
 * 收集区块事件发布者
 */
@Component
public class CollectionBlockEventPublisher {

    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.ring.buffer.size}")
    private int ringBufferSize;

    @Autowired
    private CollectionBlockEventHandler blockHandler;

    private CollectionBlockEventProducerWithTranslator producer;
    @PostConstruct
    private void init(){
        // 事件生产工厂
        CollectionBlockEventFactory eventFactory = new CollectionBlockEventFactory();
        // 事件处理线程生产工厂
        ThreadFactory consumeThreadFactory = Thread::new;
        Disruptor<CollectionBlockEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, consumeThreadFactory);
        // 设置事件处理器
        disruptor.handleEventsWith(blockHandler);
        // 启动Disruptor,让所有生产和消息线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        RingBuffer<CollectionBlockEvent> ringBuffer = disruptor.getRingBuffer();
        // 实例化生产者
        producer = new CollectionBlockEventProducerWithTranslator(ringBuffer);
    }

    /**
     * 发布事件
     * @param blockCF 区块CompletableFuture对象
     * @param blockCB 区块处理回调对象
     * @param receiptCF 交易回执CompletableFuture对象
     * @param receiptCB 交易回执处理回调对象
     */
    public void publish(
            CompletableFuture<PlatonBlock> blockCF,
            ConsumeCallback<PlatonBlock> blockCB,
            CompletableFuture<ReceiptResult> receiptCF,
            ConsumeCallback<ReceiptResult> receiptCB
    ){
        CollectionBlockEventBody eb = new CollectionBlockEventBody();
        eb.setBlockCF(blockCF);
        eb.setBlockCB(blockCB);
        eb.setReceiptCF(receiptCF);
        eb.setReceiptCB(receiptCB);
        producer.onData(eb);
    }
}
