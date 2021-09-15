package com.platon.browser.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.bean.ComplementEvent;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.handler.ComplementEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 补充事件发布者
 */
@Slf4j
@Component
public class ComplementEventPublisher extends AbstractPublisher<ComplementEvent> {

    private static final EventTranslatorVararg<ComplementEvent>
            TRANSLATOR = (event, sequence, args) -> {
        event.setBlock((Block) args[0]);
        event.setTransactions((List<Transaction>) args[1]);
        event.setNodeOpts((List<NodeOpt>) args[2]);
        event.setDelegationRewards((List<DelegationReward>) args[3]);
        event.setTraceId((String) args[4]);
    };

    @Override
    public int getRingBufferSize() {
        return config.getComplementBufferSize();
    }

    private EventFactory<ComplementEvent> eventFactory = ComplementEvent::new;

    @Resource
    private ComplementEventHandler complementEventHandler;

    @PostConstruct
    private void init() {
        Disruptor<ComplementEvent> disruptor = new Disruptor<>(eventFactory, getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(complementEventHandler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(ComplementEventPublisher.class.getSimpleName(), this);
    }

    public void publish(Block block, List<Transaction> transactions, List<NodeOpt> nodeOpts, List<DelegationReward> delegationRewards, String traceId) {
        ringBuffer.publishEvent(TRANSLATOR, block, transactions, nodeOpts, delegationRewards, traceId);
    }

}
