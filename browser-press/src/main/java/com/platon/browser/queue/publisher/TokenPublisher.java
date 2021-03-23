package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.queue.event.TokenEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.TokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class TokenPublisher extends AbstractPublisher {

    private static final EventTranslatorOneArg<TokenEvent, TokenEvent> TRANSLATOR = (event, sequence, arg0) -> {
        event.setTokenList(arg0.getTokenList());
        event.setErc20TxList(arg0.getErc20TxList());
        event.setErc721TxList(arg0.getErc721TxList());
    };

    @Resource
    private TokenHandler tokenHandler;

    @Value("${disruptor.queue.token.buffer-size}")
    private int ringBufferSize;

    @Override
    AbstractHandler getHandler() {
        return tokenHandler;
    }

    @Override
    EventFactory getEventFactory() {
        return TokenEvent::new;
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
