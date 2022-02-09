package com.platon.browser.service.elasticsearch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.elasticsearch.dto.*;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.LongSummaryStatistics;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
    private EsDelegateRewardService esDelegateRewardService;

    @Resource
    private EsErc20TxService esErc20TxService;

    @Resource
    private EsErc721TxService esErc721TxService;

    private static final int SERVICE_COUNT = 5;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(SERVICE_COUNT);

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    /**
     * 是否需要重试
     */
    private AtomicBoolean isRetry = new AtomicBoolean(false);

    private <T> void submit(EsService<T> service, Set<T> data, CountDownLatch latch, ESKeyEnum eSKeyEnum, String traceId) {
        EXECUTOR.submit(() -> {
            try {
                CommonUtil.putTraceId(traceId);
                service.save(data);
                statisticsLog(data, eSKeyEnum);
                isRetry.set(false);
            } catch (IOException e) {
                isRetry.set(true);
                log.error(StrUtil.format("ES[{}]批量入库异常", eSKeyEnum.name()), e);
            } finally {
                latch.countDown();
                CommonUtil.removeTraceId();
            }
        });
    }

    @Retryable(value = BusinessException.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions, Set<DelegationReward> delegationRewards) throws Exception {
        Set<ErcTx> erc20TxList = getErc20TxList(transactions);
        Set<ErcTx> erc721TxList = getErc721TxList(transactions);
        if (log.isDebugEnabled()) {
            log.debug("ES batch import: {}(blocks({}), transactions({}), delegationRewards({}), erc20TxList({}), erc721TxList({}))",
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      blocks.size(),
                      transactions.size(),
                      delegationRewards.size(),
                      erc20TxList.size(),
                      erc721TxList.size());
        }
        try {
            long startTime = System.currentTimeMillis();
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);
            submit(esBlockService, blocks, latch, ESKeyEnum.Block, CommonUtil.getTraceId());
            submit(esTransactionService, transactions, latch, ESKeyEnum.Transaction, CommonUtil.getTraceId());
            submit(esDelegateRewardService, delegationRewards, latch, ESKeyEnum.DelegateReward, CommonUtil.getTraceId());
            submit(esErc20TxService, erc20TxList, latch, ESKeyEnum.Erc20Tx, CommonUtil.getTraceId());
            submit(esErc721TxService, erc721TxList, latch, ESKeyEnum.Erc721Tx, CommonUtil.getTraceId());
            latch.await();
            if (isRetry.get()) {
                LongSummaryStatistics blockSum = blocks.stream().collect(Collectors.summarizingLong(Block::getNum));
                throw new Exception(StrUtil.format("ES相关区块[{}]-[{}]信息批量入库异常，重试[{}]次", blockSum.getMin(), blockSum.getMax(), retryCount.incrementAndGet()));
            } else {
                retryCount.set(0);
            }
            log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("入库ES异常", e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Retryable(value = BusinessException.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions, Set<ErcTx> erc20TxList, Set<ErcTx> erc721TxList, Set<DelegationReward> delegationRewards) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("ES batch import: {}(blocks({}), transactions({}), delegationRewards({}), erc20TxList({}), erc721TxList({}))",
                      Thread.currentThread().getStackTrace()[1].getMethodName(),
                      blocks.size(),
                      transactions.size(),
                      delegationRewards.size(),
                      erc20TxList.size(),
                      erc721TxList.size());
        }
        try {
            long startTime = System.currentTimeMillis();
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);
            submit(esBlockService, blocks, latch, ESKeyEnum.Block, CommonUtil.getTraceId());
            submit(esTransactionService, transactions, latch, ESKeyEnum.Transaction, CommonUtil.getTraceId());
            submit(esDelegateRewardService, delegationRewards, latch, ESKeyEnum.DelegateReward, CommonUtil.getTraceId());
            submit(esErc20TxService, erc20TxList, latch, ESKeyEnum.Erc20Tx, CommonUtil.getTraceId());
            submit(esErc721TxService, erc721TxList, latch, ESKeyEnum.Erc721Tx, CommonUtil.getTraceId());
            latch.await();
            if (isRetry.get()) {
                LongSummaryStatistics blockSum = blocks.stream().collect(Collectors.summarizingLong(Block::getNum));
                throw new Exception(StrUtil.format("ES相关区块[{}]-[{}]信息批量入库异常，重试[{}]次", blockSum.getMin(), blockSum.getMax(), retryCount.incrementAndGet()));
            } else {
                retryCount.set(0);
            }
            log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("入库ES异常", e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 取erc20交易列表
     */
    public Set<ErcTx> getErc20TxList(Set<Transaction> transactions) {
        Set<ErcTx> result = new HashSet<>();
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction tx : transactions) {
                if (tx.getErc20TxList().isEmpty()) continue;
                result.addAll(tx.getErc20TxList());
            }
        }
        return result;
    }

    /**
     * 取erc721交易列表
     */
    public Set<ErcTx> getErc721TxList(Set<Transaction> transactions) {
        Set<ErcTx> result = new HashSet<>();
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction tx : transactions) {
                if (tx.getErc721TxList().isEmpty()) continue;
                result.addAll(tx.getErc721TxList());
            }
        }
        return result;
    }

    /**
     * 打印统计信息
     *
     * @param data
     * @param eSKeyEnum
     * @return void
     * @date 2021/5/21
     */
    private <T> void statisticsLog(Set<T> data, ESKeyEnum eSKeyEnum) {
        try {
            if (eSKeyEnum.compareTo(ESKeyEnum.Block) == 0) {
                if (CollUtil.isNotEmpty(data)) {
                    LongSummaryStatistics blockSum = ((Set<Block>) data).stream().collect(Collectors.summarizingLong(Block::getNum));
                    log.info("ES批量入库成功统计:区块[{}]-[{}]", blockSum.getMin(), blockSum.getMax());
                } else {
                    log.info("ES批量入库成功统计:区块[{}]-[{}]", 0, 0);
                }
            } else if (eSKeyEnum.compareTo(ESKeyEnum.Transaction) == 0) {
                log.info("ES批量入库成功统计:交易数[{}]", data.size());
            } else if (eSKeyEnum.compareTo(ESKeyEnum.Erc20Tx) == 0) {
                log.info("ES批量入库成功统计:erc20交易数[{}]", data.size());
            } else if (eSKeyEnum.compareTo(ESKeyEnum.Erc721Tx) == 0) {
                log.info("ES批量入库成功统计:erc721交易数[{}]", data.size());
            }
        } catch (Exception e) {
            log.error("ES批量入库成功统计打印异常", e);
        }
    }

}
