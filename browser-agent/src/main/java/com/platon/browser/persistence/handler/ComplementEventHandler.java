package com.platon.browser.persistence.handler;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.persistence.queue.publisher.PersistenceEventPublisher;
import com.platon.browser.persistence.service.rmdb.DbService;
import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 区块事件处理器
 */
@Slf4j
public class ComplementEventHandler implements IComplementEventHandler {

    @Autowired
    private DbService dbService;

    @Autowired
    private PersistenceEventPublisher persistenceEventPublisher;

    private Long preBlockNum=0L;
    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        log.debug("ComplementEvent处理:{}(event(block({}),transactions({}),businessParams({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlock().getNum(),event.getTransactions().size(),event.getBusinessParams().size(),sequence,endOfBatch);
        if(preBlockNum!=0L&&(event.getBlock().getNum()-preBlockNum!=1)) throw new AssertionError();

        try {
            // 此处调用 persistence模块功能
            event.getBlock().setTransactions(null);
            List<BusinessParam> businessParams = event.getBusinessParams();

            // TODO: 补充交易信息

            // 发布至持久化队列
            persistenceEventPublisher.publish(event.getBlock(),new ArrayList<>(event.getTransactions()));
            // 入库MYSQL
            dbService.insert(businessParams);

            preBlockNum=event.getBlock().getNum();
        }catch (Exception e){
            log.error("{}",e);
            throw e;
        }
    }
}