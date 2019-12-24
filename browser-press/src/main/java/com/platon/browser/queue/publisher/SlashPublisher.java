package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.queue.event.SlashEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.SlashHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/12/23
 * @Description:
 */
@Slf4j
@Component
public class SlashPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<SlashEvent,List<Slash>>
            TRANSLATOR = (event, sequence, slashList)->{
        event.setSlashList(slashList);
    };
    @Value("${disruptor.queue.slash.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<SlashEvent> eventFactory = SlashEvent::new;
    @Autowired
    private SlashHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<SlashEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Slash> slashList){
        ringBuffer.publishEvent(TRANSLATOR,slashList);
    }
}
