package com.platon.browser.collection.producer;

import com.platon.browser.queue.event.collection.CollectionBlockEventBody;

public interface EventProducer {
    void publish(CollectionBlockEventBody eventBody);
}
