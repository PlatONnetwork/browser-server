package com.platon.browser.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.bean.BlockEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.handler.BlockEventHandler;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 区块事件生产者
 */
@Slf4j
@Component
public class BlockEventPublisher extends AbstractPublisher<BlockEvent> {

    private static final EventTranslatorVararg<BlockEvent>
            TRANSLATOR = (event, sequence, args) -> {
        event.setBlockCF((CompletableFuture<PlatonBlock>) args[0]);
        event.setReceiptCF((CompletableFuture<ReceiptResult>) args[1]);
        event.setEpochMessage((EpochMessage) args[2]);
        event.setTraceId((String) args[3]);
    };

    @Override
    public int getRingBufferSize() {
        return config.getBlockBufferSize();
    }

    private EventFactory<BlockEvent> eventFactory = BlockEvent::new;

    @Resource
    private BlockEventHandler blockEventHandler;

    @PostConstruct
    public void init() {
        Disruptor<BlockEvent> disruptor = new Disruptor<>(eventFactory, getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(blockEventHandler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(BlockEventPublisher.class.getSimpleName(), this);
    }

    public void publish(CompletableFuture<PlatonBlock> blockCF, CompletableFuture<ReceiptResult> receiptCF, EpochMessage epochMessage, String traceId) {
        ringBuffer.publishEvent(TRANSLATOR, blockCF, receiptCF, epochMessage, traceId);
    }

}
