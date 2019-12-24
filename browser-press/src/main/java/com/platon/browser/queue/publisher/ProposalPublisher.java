package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.queue.event.ProposalEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.ProposalHandler;
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
public class ProposalPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<ProposalEvent,List<Proposal>>
            TRANSLATOR = (event, sequence, proposalList)->{
        event.setProposalList(proposalList);
    };
    @Value("${disruptor.queue.proposal.buffer-size}")
    private int ringBufferSize;
    protected RingBuffer ringBuffer;
    private EventFactory<ProposalEvent> eventFactory = ProposalEvent::new;
    @Autowired
    private ProposalHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @PostConstruct
    private void init(){
        Disruptor<ProposalEvent> disruptor = new Disruptor<>(eventFactory, ringBufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void publish(List<Proposal> proposalList){
        ringBuffer.publishEvent(TRANSLATOR,proposalList);
    }
}
