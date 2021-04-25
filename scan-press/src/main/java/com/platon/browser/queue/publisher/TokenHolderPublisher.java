package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.TokenHolder;
import com.platon.browser.queue.event.TokenHolderEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.TokenHolderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class TokenHolderPublisher extends AbstractPublisher {

    private static final EventTranslatorOneArg<TokenHolderEvent, List<TokenHolder>> TRANSLATOR = (event, sequence, msg) -> event.setTokenList(msg);

    @Value("${disruptor.queue.token-holder.buffer-size}")
    private int ringBufferSize;

    @Resource
    private TokenHolderHandler tokenHolderHandler;

    @Override
    AbstractHandler getHandler() {
        return tokenHolderHandler;
    }

    @Override
    EventFactory getEventFactory() {
        return TokenHolderEvent::new;
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
