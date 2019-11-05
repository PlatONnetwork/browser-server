package com.platon.browser.persistence.handler;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.persistence.queue.publisher.PersistenceEventPublisher;
import com.platon.browser.persistence.service.rmdb.DbService;
import com.platon.browser.queue.complement.event.ComplementEvent;
import com.platon.browser.queue.complement.handler.IComplementEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 区块事件处理器
 */
@Slf4j
public class ComplementEventHandler implements IComplementEventHandler {

    @Autowired
    private DbService dbService;

    @Autowired
    private PersistenceEventPublisher persistenceEventPublisher;

    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        try {
            // 此处调用 persistence模块功能
            event.getBlock().setTransactions(null);
            List<BusinessParam> businessParams = event.getBusinessParams();
            // 入库MYSQL
            dbService.insert(businessParams);

            // TODO: 补充交易信息

            // 发布至持久化队列
            persistenceEventPublisher.publish(event.getBlock(),new ArrayList<>(event.getTransactions()));
        }catch (Exception e){
            log.error("{}",e);
            throw e;
        }
    }
}