package com.platon.browser.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorThreeArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.handler.BlockEventHandler;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.bean.BlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.platon.protocol.core.methods.response.PlatonBlock;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 区块事件生产者
 */
@Slf4j
@Component
public class BlockEventPublisher extends AbstractPublisher<BlockEvent> {
    private static final EventTranslatorThreeArg<BlockEvent,CompletableFuture<PlatonBlock>,CompletableFuture<ReceiptResult>,EpochMessage>
    TRANSLATOR = (event, sequence, blockCF,receiptCF,epochMessage)->{
        event.setBlockCF(blockCF);
        event.setReceiptCF(receiptCF);
        event.setEpochMessage(epochMessage);
    };
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }

    private EventFactory<BlockEvent> eventFactory = BlockEvent::new;
    @Resource
    private BlockEventHandler blockEventHandler;

    @PostConstruct
    public void init(){
        Disruptor<BlockEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(blockEventHandler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(BlockEventPublisher.class.getSimpleName(),this);
    }

    public void publish(CompletableFuture<PlatonBlock> blockCF,CompletableFuture<ReceiptResult> receiptCF,EpochMessage epochMessage){
        ringBuffer.publishEvent(TRANSLATOR, blockCF,receiptCF,epochMessage);
    }
}
