package com.platon.browser.collection.producer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import com.platon.browser.queue.event.collection.CollectionBlockEventBody;

/**
 * 区块事件生产者
 */
public class CollectionBlockEventProducerWithTranslator implements EventProducer {
    private final RingBuffer<CollectionBlockEvent> ringBuffer;
    //一个translator可以看做一个事件初始化器，publicEvent方法会调用它
    //填充Event
    private final EventTranslatorOneArg<CollectionBlockEvent, CollectionBlockEventBody> translator = (event, sequence, eventBody) -> event.setBody(eventBody);
    public CollectionBlockEventProducerWithTranslator(RingBuffer<CollectionBlockEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void publish(CollectionBlockEventBody eventBody) {
        ringBuffer.publishEvent(translator, eventBody);
    }
}
