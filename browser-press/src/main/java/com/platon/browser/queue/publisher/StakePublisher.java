package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.queue.event.NodeEvent;
import com.platon.browser.queue.event.StakeEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.NodeHandler;
import com.platon.browser.queue.handler.StakeHandler;
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
public class StakePublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<StakeEvent,List<Staking>>
            TRANSLATOR = (event, sequence, Staking)->{
        event.setStakingList(Staking);
    };
    @Value("${disruptor.queue.block.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<StakeEvent> eventFactory = StakeEvent::new;
    @Autowired
    private StakeHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<StakeEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Staking> nodeList){
        ringBuffer.publishEvent(TRANSLATOR,nodeList);
    }
}
