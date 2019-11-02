package com.platon.browser.collection.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.collection.queue.event.BlockEvent;
import com.platon.browser.collection.queue.handler.BlockEventHandler;
import com.platon.browser.common.collection.dto.EpochMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;

/**
 * 区块事件生产者
 */
@Slf4j
@Component
public class BlockEventPublisher {
    private static final EventTranslatorThreeArg<BlockEvent,CompletableFuture<PlatonBlock>,CompletableFuture<ReceiptResult>,EpochMessage>
    TRANSLATOR = (event, sequence, blockCF,receiptCF,epochMessage)->event.setBlockCF(blockCF).setReceiptCF(receiptCF).setEpochMessage(epochMessage);
    private RingBuffer<BlockEvent> ringBuffer;
    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    private EventFactory<BlockEvent> eventFactory = () -> BlockEvent.builder().build();
    @Autowired
    private BlockEventHandler handler;
    // 事件处理线程生产工厂
    ThreadFactory consumeThreadFactory = Thread::new;

    @PostConstruct
    private void init(){
        Disruptor<BlockEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, consumeThreadFactory);
        // 设置事件处理器
        disruptor.handleEventsWith(handler);
        // 启动Disruptor,让所有生产和消费线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(CompletableFuture<PlatonBlock> blockCF,CompletableFuture<ReceiptResult> receiptCF,EpochMessage epochMessage){
        ringBuffer.publishEvent(TRANSLATOR, blockCF,receiptCF,epochMessage);
    }
}
