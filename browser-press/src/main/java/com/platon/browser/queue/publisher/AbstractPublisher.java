package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.queue.event.VoteEvent;
import com.platon.browser.queue.handler.AbstractHandler;

import javax.annotation.PostConstruct;

public abstract class AbstractPublisher {
    protected RingBuffer ringBuffer;

    abstract AbstractHandler getHandler();
    abstract EventFactory getEventFactory();
    abstract int getRingBufferSize();
    abstract EventTranslatorOneArg getTranslator();

    public long getTotalCount() {
        return getHandler().getTotalCount();
    }
    public void setTotalCount(long totalCount) {
        getHandler().setTotalCount(totalCount);
    }

    @PostConstruct
    private void init(){
        Disruptor<VoteEvent> disruptor = new Disruptor<>(getEventFactory(), getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(getHandler());
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public <T> void publish(T msg){
        ringBuffer.publishEvent(getTranslator(),msg);
    }
}
