package com.platon.browser.common.service.redis;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.redis.RedisBlockService;
import com.platon.browser.service.redis.RedisService;
import com.platon.browser.service.redis.RedisStatisticService;
import com.platon.browser.service.redis.RedisTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Redis数据批量入库服务
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class RedisImportService {
    @Autowired
    private RedisBlockService blockService;
    @Autowired
    private RedisTransactionService transactionService;
    @Autowired
    private RedisStatisticService statisticService;

    private static final int SERVICE_COUNT = 3;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(SERVICE_COUNT);

    private <T> void submit(RedisService<T> service, Set<T> data, boolean serialOverride, CountDownLatch latch){
        EXECUTOR.submit(()->{
            try {
                service.save(data,serialOverride);
            } catch (Exception e) {
                log.error("",e);
            }finally {
                latch.countDown();
            }
        });
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions, Set<NetworkStat> statistics) throws InterruptedException {
        log.debug("Redis批量导入:{}(blocks({}),transactions({}),statistics({})",Thread.currentThread().getStackTrace()[1].getMethodName(),blocks.size(),transactions.size(),statistics.size());
        long startTime = System.currentTimeMillis();
        try{
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);
            submit(blockService,blocks,false,latch);
            submit(transactionService,transactions,false,latch);
            submit(statisticService,statistics,true,latch);
            latch.await();
            log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}
