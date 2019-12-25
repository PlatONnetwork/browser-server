package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Transaction;
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
import java.util.*;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class TransactionHandler  extends AbstractHandler<TransactionEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Autowired
    private AddressPublisher addressPublisher;

    @Value("${platon.addressMaxCount}")
    private long addressMaxCount;

    @Getter
    @Setter
    private long currentAddressSum = 0L;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    private Set<Transaction> stage = new HashSet<>();

    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        try {
            stage.addAll(event.getTransactionList());
            if(stage.size()<batchSize) return;

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(Collections.emptySet(),stage,Collections.emptySet());

            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            redisImportService.batchImport(Collections.emptySet(),stage,statistics);

            long endTime = System.currentTimeMillis();
            printTps("交易",stage.size(),startTime,endTime);

            if(currentAddressSum<addressMaxCount){
                // 地址数量未达到指定数量，则继续入库
                List<Address> addressList = new ArrayList<>();
                stage.forEach(tx->{
                    Address address=new Address();
                    address.setAddress(tx.getFrom());
                    addressList.add(address);
                    address=new Address();
                    address.setAddress(tx.getTo());
                    addressList.add(address);
                });
                addressPublisher.publish(addressList);
                currentAddressSum=currentAddressSum+addressList.size();
            }
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}