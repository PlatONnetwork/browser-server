package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.queue.event.AddressEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.AddressHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class AddressPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<AddressEvent,List<Address>> TRANSLATOR = (event, sequence, msg)->event.setAddressList(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private AddressHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.address.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return AddressEvent::new;
    }
}
