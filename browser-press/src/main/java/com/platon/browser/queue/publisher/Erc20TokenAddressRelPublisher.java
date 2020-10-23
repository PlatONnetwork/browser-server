package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.Erc20TokenAddressRel;
import com.platon.browser.queue.event.Erc20TokenAddressRelEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.Erc20TokenAddressRelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Erc20TokenAddressRelPublisher extends AbstractPublisher {

    private static final EventTranslatorOneArg<Erc20TokenAddressRelEvent, List<Erc20TokenAddressRel>> TRANSLATOR =
            (event, sequence, msg) -> event.setErc20TokenAddressRelList(msg);

    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private Erc20TokenAddressRelHandler handler;

    @Override
    public AbstractHandler getHandler() {
        return handler;
    }

    @Value("${disruptor.queue.token-address.buffer-size}")
    private int ringBufferSize;

    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return Erc20TokenAddressRelEvent::new;
    }
}
