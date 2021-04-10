package com.platon.browser.queue.publisher;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.platon.browser.queue.event.Event;
import com.platon.browser.queue.handler.AbstractHandler;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
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
    private void init() {
        Disruptor<Event> disruptor = new Disruptor<>(getEventFactory(), getRingBufferSize(), DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(getHandler());
        disruptor.setDefaultExceptionHandler(new ExceptionHandler<Event>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, Event event) {
                log.error("handleEvent event：{}", event);
                log.error("handleEvent", ex);
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                log.error("handleOnStart", ex);
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                log.error("handleOnShutdown", ex);
            }
        });
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();

    }

    public <T> void publish(T msg) {
        ringBuffer.publishEvent(getTranslator(), msg);
    }

}
