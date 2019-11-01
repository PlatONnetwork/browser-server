package com.platon.browser.complement.handler;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.queue.collection.event.CollectionEvent;
import com.platon.browser.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.queue.complement.publisher.ComplementEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class CollectionEventHandler implements ICollectionEventHandler {

    @Autowired
    private ComplementEventPublisher complementEventPublisher;

    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) {

        // 此处调用 complement模块的功能
        List<BusinessParam> businessParams = new ArrayList<>();

        complementEventPublisher.publish(event.getBlock(),event.getTransactions(),businessParams);
    }
}