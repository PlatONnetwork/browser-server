package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.publisher.AddressPublisher;
import com.platon.browser.service.elasticsearch.EsTransactionService;
import com.platon.browser.service.redis.RedisTransactionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class TransactionHandler  extends AbstractHandler<TransactionEvent> {

    @Autowired
    private EsTransactionService esTransactionService;
    @Autowired
    private RedisTransactionService redisTransactionService;

    @Autowired
    private AddressPublisher addressPublisher;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    private StageCache<Transaction> stage = new StageCache<>();
    @PostConstruct
    private void init(){
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<Transaction> cache = stage.getData();
        try {
            cache.addAll(event.getTransactionList());
            if(cache.size()<batchSize) return;
            esTransactionService.save(stage);
            redisTransactionService.save(cache,false);
            long endTime = System.currentTimeMillis();
            printTps("交易",cache.size(),startTime,endTime);

            // 地址数量未达到指定数量，则继续入库
            List<Address> addressList = new ArrayList<>();
            cache.forEach(tx->{
                Address address=new Address();
                address.setAddress(tx.getFrom());
                addressList.add(address);
                address=new Address();
                address.setAddress(tx.getTo());
                addressList.add(address);
            });
            addressPublisher.publish(addressList);

            cache.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}