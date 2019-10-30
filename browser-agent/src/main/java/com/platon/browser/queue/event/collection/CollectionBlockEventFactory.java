package com.platon.browser.queue.event.collection;


import com.lmax.disruptor.EventFactory;

/**
 * 区块搜集事件工厂
 */
public class CollectionBlockEventFactory implements EventFactory<CollectionBlockEvent> {
    @Override
    public CollectionBlockEvent newInstance() {
        return new CollectionBlockEvent();
    }
}