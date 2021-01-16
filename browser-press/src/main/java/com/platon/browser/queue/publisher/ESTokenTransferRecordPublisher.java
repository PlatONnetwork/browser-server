package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.elasticsearch.dto.OldErcTx;
import com.platon.browser.queue.event.ESTokenTransferRecordEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.OldErc20TxHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ESTokenTransferRecordPublisher extends AbstractPublisher {

    private static final EventTranslatorOneArg<ESTokenTransferRecordEvent, List<OldErcTx>> TRANSLATOR =
            (event, sequence, msg) -> event.setOldErcTxList(msg);

    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private OldErc20TxHandler handler;

    @Override
    public AbstractHandler getHandler() {
        return handler;
    }

    @Value("${disruptor.queue.token-transfer.buffer-size}")
    private int ringBufferSize;

    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return ESTokenTransferRecordEvent::new;
    }
}
