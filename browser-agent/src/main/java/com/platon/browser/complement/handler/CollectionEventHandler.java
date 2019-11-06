package com.platon.browser.complement.handler;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.complement.service.param.BlockParameterService;
import com.platon.browser.complement.service.param.TransactionParameterService;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.collection.event.CollectionEvent;
import com.platon.browser.queue.collection.handler.ICollectionEventHandler;
import com.platon.browser.queue.complement.publisher.ComplementEventPublisher;

import lombok.extern.slf4j.Slf4j;

/**
 * 区块事件处理器
 */
@Slf4j
public class CollectionEventHandler implements ICollectionEventHandler {

    @Autowired
    private TransactionParameterService transactionParameterService;
    @Autowired
    private BlockParameterService blockParameterService;
    @Autowired
    private ComplementEventPublisher complementEventPublisher;

    // TODO: 启动时需要使用初始化数据初始化交易序号id
    private long transactionId = 0;
    
    
    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            // 确保交易从小到大的索引顺序
            event.getTransactions().sort(Comparator.comparing(Transaction::getIndex));
            for (CollectionTransaction tx : event.getTransactions()) tx.setId(++transactionId);

            // 根据区块号解析出业务参数
            List<BusinessParam> param1 = blockParameterService.getParameters(event);
            // 根据交易解析出业务参数
            List<BusinessParam> param2 = transactionParameterService.getParameters(event);
            param1.addAll(param2);

            complementEventPublisher.publish(event.getBlock(),event.getTransactions(),param1);
        }catch (Exception e){
            log.error("{}",e);
            throw e;
        }
    }
}