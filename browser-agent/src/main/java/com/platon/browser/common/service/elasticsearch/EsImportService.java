package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class EsImportService {
    @Autowired
    private EsBlockService blockService;
    @Autowired
    private EsTransactionService transactionService;
    @Autowired
    private EsNodeOptService nodeOptService;
    @Autowired
    private EsDelegateRewardService delegateRewardService;

    @Autowired
    private EsTokenTransferRecordService esTokenTransferRecordService;

    private static final int SERVICE_COUNT = 5;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(SERVICE_COUNT);

    private <T> void submit(EsService<T> service, Set<T> data, CountDownLatch latch) {
        EXECUTOR.submit(() -> {
            try {
                service.save(data);
            } catch (IOException e) {
                log.error("", e);
            } finally {
                latch.countDown();
            }
        });
    }
    
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions,
                            Set<NodeOpt> nodeOpts, Set<DelegationReward> delegationRewards) throws InterruptedException {

        Set<ESTokenTransferRecord> recordSet = retryRecordSet(transactions);
        if (log.isDebugEnabled()) {
            log.debug("ES batch import: {}(blocks({}), transactions({}), nodeOpts({}), delegationRewards({}), tokenTransfer({}))",
                    Thread.currentThread().getStackTrace()[1].getMethodName(), blocks.size(), transactions.size(),
                    nodeOpts.size(), delegationRewards.size(), recordSet.size());
        }
        try{
            long startTime = System.currentTimeMillis();
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);

            submit(blockService, blocks, latch);
            submit(transactionService, transactions, latch);
            submit(nodeOptService, nodeOpts, latch);
            submit(delegateRewardService, delegationRewards, latch);
            submit(esTokenTransferRecordService, recordSet, latch);
            latch.await();

            log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }

    /**
     * retry transfer record from transactions.
     */
    public Set<ESTokenTransferRecord> retryRecordSet(Set<Transaction> txSet){
        Set<ESTokenTransferRecord> recordSet = new HashSet<>();
        if (txSet != null && txSet.size() != 0) {
            Iterator<Transaction> transactionIterator = txSet.iterator();
            while (transactionIterator.hasNext()) {
                Transaction tx = transactionIterator.next();
                if (null != tx && null != tx.getEsTokenTransferRecords() && tx.getEsTokenTransferRecords().size() != 0) {
                    recordSet.addAll(tx.getEsTokenTransferRecords());
                }
            }
        }
        return recordSet;
    }
}
