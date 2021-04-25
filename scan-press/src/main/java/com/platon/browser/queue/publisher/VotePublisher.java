package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.queue.event.VoteEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.VoteHandler;
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
public class VotePublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<VoteEvent, List<Vote>> TRANSLATOR = (event, sequence, msg)->event.setVoteList(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private VoteHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.vote.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return VoteEvent::new;
    }
}
