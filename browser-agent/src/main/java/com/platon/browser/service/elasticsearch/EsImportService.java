package com.platon.browser.service.elasticsearch;

import com.platon.browser.elasticsearch.dto.*;
import com.platon.browser.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
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
    @Resource
    private EsBlockService esBlockService;
    @Resource
    private EsTransactionService esTransactionService;
    @Resource
    private EsNodeOptService esNodeOptService;
    @Resource
    private EsDelegateRewardService esDelegateRewardService;
    @Resource
    private OldEsErc20TxService oldEsErc20TxService;
    @Resource
    private EsErc20TxService esErc20TxService;
    @Resource
    private EsErc721TxService esErc721TxService;

    private static final int SERVICE_COUNT = 7;

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

    @Retryable(value = BusinessException.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions,
                            Set<NodeOpt> nodeOpts, Set<DelegationReward> delegationRewards) throws InterruptedException {

        Set<OldErcTx> oldErc20TxList = getOldErc20TxList(transactions);
        Set<EsErcTx> erc20TxList = getErc20TxList(transactions);
        Set<EsErcTx> erc721TxList = getErc721TxList(transactions);
        if (log.isDebugEnabled()) {
            log.debug("ES batch import: {}(blocks({}), transactions({}), nodeOpts({}), delegationRewards({}), oldErc20TxList({}), erc20TxList({}), erc721TxList({}))",
                    Thread.currentThread().getStackTrace()[1].getMethodName(), blocks.size(), transactions.size(),
                    nodeOpts.size(), delegationRewards.size(), oldErc20TxList.size(), erc20TxList.size(), erc721TxList.size());
        }
        try{
            long startTime = System.currentTimeMillis();
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);

            submit(esBlockService, blocks, latch);
            submit(esTransactionService, transactions, latch);
            submit(esNodeOptService, nodeOpts, latch);
            submit(esDelegateRewardService, delegationRewards, latch);
            submit(oldEsErc20TxService, oldErc20TxList, latch);
            submit(esErc20TxService, erc20TxList, latch);
            submit(esErc721TxService, erc721TxList, latch);
            latch.await();

            log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
        }catch (Exception e){
            log.error("",e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 取Token交易列表
     */
    public Set<OldErcTx> getOldErc20TxList(Set<Transaction> txSet){
        Set<OldErcTx> recordSet = new HashSet<>();
        if (txSet != null && !txSet.isEmpty()) {
            for (Transaction tx : txSet) {
                if (null != tx && null != tx.getOldErcTxes() && !tx.getOldErcTxes().isEmpty()) {
                    recordSet.addAll(tx.getOldErcTxes());
                }
            }
        }
        return recordSet;
    }

    /**
     * 取erc20交易列表
     */
    public Set<EsErcTx> getErc20TxList(Set<Transaction> transactions){
        Set<EsErcTx> result = new HashSet<>();
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction tx : transactions) {
                if (null != tx && null != tx.getOldErcTxes() && !tx.getOldErcTxes().isEmpty()) {
                    tx.getOldErcTxes().forEach(e->{
                        EsErcTx ercTx = new EsErcTx();
                        BeanUtils.copyProperties(e,ercTx);
                        result.add(ercTx);
                    });
                }
            }
        }
        return result;
    }

    /**
     * 取erc721交易列表
     */
    public Set<EsErcTx> getErc721TxList(Set<Transaction> transactions){
        // TODO: 取出erc721交易
        Set<EsErcTx> result = new HashSet<>();
//        if (transactions != null && !transactions.isEmpty()) {
//            for (Transaction tx : transactions) {
//                if (null != tx && null != tx.getEsTokenTransferRecords() && !tx.getEsTokenTransferRecords().isEmpty()) {
//                    tx.getEsTokenTransferRecords().forEach(e->{
//                        EsErcTx ercTx = new EsErcTx();
//                        BeanUtils.copyProperties(e,ercTx);
//                        result.add(ercTx);
//                    });
//                }
//            }
//        }
        return result;
    }
}
