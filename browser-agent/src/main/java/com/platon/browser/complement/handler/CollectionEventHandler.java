package com.platon.browser.complement.handler;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.complement.service.BusinessService;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.collection.event.CollectionEvent;
import com.platon.browser.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.queue.complement.publisher.ComplementEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class CollectionEventHandler implements ICollectionEventHandler {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ComplementEventPublisher complementEventPublisher;

    // TODO: 启动时需要使用初始化数据初始化
    private long transactionId = 0;

    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            // 确保交易从小到大的索引顺序
            event.getTransactions().sort(Comparator.comparing(Transaction::getIndex));
            for (CollectionTransaction tx : event.getTransactions()) tx.setId(++transactionId);

            // 此处调用 complement模块的功能, 解析出业务参数
            List<BusinessParam> businessParams = businessService.getParameters(event.getTransactions());

            complementEventPublisher.publish(event.getBlock(),event.getTransactions(),businessParams);
        }catch (Exception e){
            log.error("{}",e);
            throw e;
        }
    }
}