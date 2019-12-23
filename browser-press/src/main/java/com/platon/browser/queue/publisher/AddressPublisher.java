package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.event.AddressEvent;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.queue.handler.AddressHandler;
import com.platon.browser.queue.handler.BlockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
public class AddressPublisher {
    private static final EventTranslatorOneArg<AddressEvent,List<Address>>
            TRANSLATOR = (event, sequence, addressList)->{
        event.setAddressList(addressList);
    };
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<AddressEvent> eventFactory = AddressEvent::new;
    @Autowired
    private AddressHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<AddressEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Address> addressList){
        ringBuffer.publishEvent(TRANSLATOR,addressList);
    }
}
