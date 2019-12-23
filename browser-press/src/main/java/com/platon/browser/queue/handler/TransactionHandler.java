package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.AddressEvent;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.publisher.AddressPublisher;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class TransactionHandler  extends AbstractHandler implements EventHandler<TransactionEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Autowired
    private AddressPublisher addressPublisher;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    private Set<Transaction> transactionStage = new HashSet<>();

    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        log.debug("TransactionEvent处理:{}(event(transactionList({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getTransactionList().size());
        try {
            transactionStage.addAll(event.getTransactionList());

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(transactionStage.size()<batchSize) {
                return;
            }

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(Collections.emptySet(),transactionStage,Collections.emptySet());

            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            redisImportService.batchImport(Collections.emptySet(),transactionStage,statistics);

            long endTime = System.currentTimeMillis();
            printTps("交易",transactionStage.size(),startTime,endTime);

            List <Address> addresses = new ArrayList <>();
            event.getTransactionList().forEach(tx->{
                Address address=new Address();
                address.setAddress(tx.getFrom());
                addresses.add(address);
                address=new Address();
                address.setAddress(tx.getTo());
                addresses.add(address);
            });
            addressPublisher.publish(addresses);

            transactionStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}