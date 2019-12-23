package com.platon.browser.queue.publisher;

import com.platon.browser.queue.handler.AbstractHandler;

public abstract class AbstractPublisher {
    public long getTotalCount() {
        return getHandler().getTotalCount();
    }

    public void setTotalCount(long totalCount) {
        getHandler().setTotalCount(totalCount);
    }

    abstract AbstractHandler getHandler();
}
