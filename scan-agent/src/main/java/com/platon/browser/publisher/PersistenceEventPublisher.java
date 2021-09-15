package com.platon.browser.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.bean.PersistenceEvent;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.handler.PersistenceEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 持久化事件生产者
 */
@Slf4j
@Component
public class PersistenceEventPublisher extends AbstractPublisher<PersistenceEvent> {

    private static final EventTranslatorVararg<PersistenceEvent>
            TRANSLATOR = (event, sequence, args) -> {
        event.setBlock((Block) args[0]);
        event.setTransactions((List<Transaction>) args[1]);
        event.setNodeOpts((List<NodeOpt>) args[2]);
        event.setDelegationRewards((List<DelegationReward>) args[3]);
        event.setTraceId((String) args[4]);
    };

    @Override
    public int getRingBufferSize() {
        return config.getPersistenceBufferSize();
    }

    private EventFactory<PersistenceEvent> eventFactory = PersistenceEvent::new;

    @Resource
    private PersistenceEventHandler persistenceEventHandler;

    @PostConstruct
    private void init() {
        Disruptor<PersistenceEvent> disruptor = new Disruptor<>(eventFactory, getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(persistenceEventHandler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
        register(PersistenceEventPublisher.class.getSimpleName(), this);
    }

    public void publish(Block block, List<Transaction> transactions, List<NodeOpt> nodeOpts, List<DelegationReward> delegationRewards, String traceId) {
        ringBuffer.publishEvent(TRANSLATOR, block, transactions, nodeOpts, delegationRewards, traceId);
    }

}
