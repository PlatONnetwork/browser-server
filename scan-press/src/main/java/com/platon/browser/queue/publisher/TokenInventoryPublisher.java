package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.TokenInventory;
import com.platon.browser.dao.entity.TokenInventoryWithBLOBs;
import com.platon.browser.queue.event.TokenInventoryEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.TokenInventoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class TokenInventoryPublisher extends AbstractPublisher {

    private static final EventTranslatorOneArg<TokenInventoryEvent, List<TokenInventoryWithBLOBs>> TRANSLATOR = (event, sequence, msg) -> event.setTokenList(msg);

    @Value("${disruptor.queue.token-inventory.buffer-size}")
    private int ringBufferSize;

    @Resource
    private TokenInventoryHandler tokenInventoryHandler;

    @Override
    AbstractHandler getHandler() {
        return tokenInventoryHandler;
    }

    @Override
    EventFactory getEventFactory() {
        return TokenInventoryEvent::new;
    }

    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

}
