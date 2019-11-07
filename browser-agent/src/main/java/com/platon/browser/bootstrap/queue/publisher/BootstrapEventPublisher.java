package com.platon.browser.bootstrap.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.platon.browser.bootstrap.queue.callback.Callback;
import com.platon.browser.bootstrap.queue.event.BootstrapEvent;
import com.platon.browser.bootstrap.queue.handler.BootstrapEventHandler;
import com.platon.browser.client.result.ReceiptResult;
import lombok.Getter;
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
public class BootstrapEventPublisher {
    private static final EventTranslatorThreeArg<BootstrapEvent,CompletableFuture<PlatonBlock>,CompletableFuture<ReceiptResult>, Callback>
    TRANSLATOR = (event, sequence, blockCF,receiptCF,callback)->event.setBlockCF(blockCF).setReceiptCF(receiptCF).setCallback(callback);
    private RingBuffer<BootstrapEvent> ringBuffer;
    // 指定环形队列大小,必须是2的指数倍
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    private EventFactory<BootstrapEvent> eventFactory = () -> BootstrapEvent.builder().build();
    @Autowired
    private BootstrapEventHandler handler;
    // 事件处理线程生产工厂
    ThreadFactory consumeThreadFactory = Thread::new;

    @Getter private Disruptor<BootstrapEvent> disruptor;

    @PostConstruct
    private void init(){
        disruptor = new Disruptor<>(eventFactory, ringBufferSize, consumeThreadFactory);
        // 设置事件处理器
        disruptor.handleEventsWith(handler);
        // 启动Disruptor,让所有生产和消费线程运行
        disruptor.start();
        // 获取环形缓冲引用，用于发布事件
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(CompletableFuture<PlatonBlock> blockCF, CompletableFuture<ReceiptResult> receiptCF, Callback callback){
        ringBuffer.publishEvent(TRANSLATOR, blockCF,receiptCF,callback);
    }

    public void shutdown() {
        disruptor.shutdown();
    }
}
