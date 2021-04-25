package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.queue.event.RewardEvent;
import com.platon.browser.queue.handler.AbstractHandler;
import com.platon.browser.queue.handler.RewardHandler;
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
public class RewardPublisher extends AbstractPublisher {
    private static final EventTranslatorOneArg<RewardEvent, List<DelegationReward>> TRANSLATOR = (event, sequence, msg)->event.setDelegationRewards(msg);
    @Override
    EventTranslatorOneArg getTranslator() {
        return TRANSLATOR;
    }

    @Autowired
    private RewardHandler handler;
    @Override
    public AbstractHandler getHandler(){return handler;}

    @Value("${disruptor.queue.reward.buffer-size}")
    private int ringBufferSize;
    @Override
    int getRingBufferSize() {
        return ringBufferSize;
    }

    @Override
    EventFactory getEventFactory() {
        return RewardEvent::new;
    }
}
