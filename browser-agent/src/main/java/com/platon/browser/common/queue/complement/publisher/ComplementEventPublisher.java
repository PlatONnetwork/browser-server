package com.platon.browser.common.queue.complement.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.common.queue.AbstractPublisher;
import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 补充事件发布者
 */
@Slf4j
@Component
public class ComplementEventPublisher extends AbstractPublisher<ComplementEvent> {
    private static final EventTranslatorVararg<ComplementEvent>
    TRANSLATOR = (event, sequence,args)->{
        event.setBlock((Block)args[0]);
        event.setTransactions((List<Transaction>)args[1]);
        event.setNodeOpts((List<NodeOpt>)args[2]);
        event.setDelegationRewards((List<DelegationReward>)args[3]);
    };
    @Value("${disruptor.queue.complement.buffer-size}")
    private int ringBufferSize;
    @Override
    public int getRingBufferSize() {
        return ringBufferSize;
    }
    private EventFactory<ComplementEvent> eventFactory = ComplementEvent::new;
    @Autowired
    private IComplementEventHandler handler;

    @PostConstruct
    private void init(){
        Disruptor<ComplementEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(ComplementEventPublisher.class.getSimpleName(),this);
    }

    public void publish(Block block, List<Transaction> transactions, List<NodeOpt> nodeOpts,List<DelegationReward> delegationRewards) {
        ringBuffer.publishEvent(TRANSLATOR, block,transactions,nodeOpts,delegationRewards);
    }
}
