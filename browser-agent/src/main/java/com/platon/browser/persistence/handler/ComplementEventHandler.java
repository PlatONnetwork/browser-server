package com.platon.browser.persistence.handler;

import com.platon.browser.common.queue.complement.event.ComplementEvent;
import com.platon.browser.common.queue.complement.handler.IComplementEventHandler;
import com.platon.browser.persistence.queue.publisher.PersistenceEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * 区块事件处理器
 */
@Slf4j
public class ComplementEventHandler implements IComplementEventHandler {

    @Autowired
    private PersistenceEventPublisher persistenceEventPublisher;

    private Long preBlockNum=0L;
    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        log.debug("ComplementEvent处理:{}(event(block({}),transactions({}),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlock().getNum(),event.getTransactions().size(),sequence,endOfBatch);
        if(preBlockNum!=0L&&(event.getBlock().getNum()-preBlockNum!=1)) throw new AssertionError();

        try {
            // 此处调用 persistence模块功能
            event.getBlock().setTransactions(null);

            // 发布至持久化队列
            persistenceEventPublisher.publish(event.getBlock(),new ArrayList<>(event.getTransactions()),new ArrayList<>(event.getNodeOpts()));

            preBlockNum=event.getBlock().getNum();
        }catch (Exception e){
            log.error("{}",e);
            throw e;
        }
    }
}