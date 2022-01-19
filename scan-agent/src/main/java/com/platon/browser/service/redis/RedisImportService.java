package com.platon.browser.service.redis;

import cn.hutool.core.util.StrUtil;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
 * Redis数据批量入库服务
 *
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Service
public class RedisImportService {

    @Resource
    private RedisBlockService redisBlockService;

    @Resource
    private RedisTransactionService redisTransactionService;

    @Resource
    private RedisStatisticService redisStatisticService;

    @Resource
    private RedisErc20TxService redisErc20TxService;

    @Resource
    private RedisErc721TxService redisErc721TxService;

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

    private <T> void submit(AbstractRedisService<T> service, Set<T> data, boolean serialOverride, CountDownLatch latch, RedisKeyEnum redisKeyEnum, String traceId) {
        EXECUTOR.submit(() -> {
            try {
                CommonUtil.putTraceId(traceId);
                service.save(data, serialOverride);
                statisticsLog(data, redisKeyEnum);
                isRetry.set(false);
            } catch (Exception e) {
                isRetry.set(true);
                log.error(StrUtil.format("redis[{}]批量入库异常", redisKeyEnum.name()), e);
            } finally {
                latch.countDown();
                CommonUtil.removeTraceId();
            }
        });
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void batchImport(Set<Block> blocks, Set<Transaction> transactions, Set<NetworkStat> statistics) throws Exception {
        log.debug("Redis批量导入:{}(blocks({}),transactions({}),statistics({})", Thread.currentThread().getStackTrace()[1].getMethodName(), blocks.size(), transactions.size(), statistics.size());
        long startTime = System.currentTimeMillis();
        try {
            Set<ErcTx> erc20TxList = getErc20TxList(transactions);
            Set<ErcTx> erc721TxList = getErc721TxList(transactions);
            CountDownLatch latch = new CountDownLatch(SERVICE_COUNT);
            submit(redisBlockService, blocks, false, latch, RedisKeyEnum.Block, CommonUtil.getTraceId());
            submit(redisTransactionService, transactions, false, latch, RedisKeyEnum.Transaction, CommonUtil.getTraceId());
            submit(redisStatisticService, statistics, true, latch, RedisKeyEnum.Statistic, CommonUtil.getTraceId());
            submit(redisErc20TxService, erc20TxList, false, latch, RedisKeyEnum.Erc20Tx, CommonUtil.getTraceId());
            submit(redisErc721TxService, erc721TxList, false, latch, RedisKeyEnum.Erc721Tx, CommonUtil.getTraceId());
            latch.await();
            if (isRetry.get()) {
                LongSummaryStatistics blockSum = blocks.stream().collect(Collectors.summarizingLong(Block::getNum));
                throw new Exception(StrUtil.format("redis相关区块[{}]-[{}]信息批量入库异常，重试[{}]次", blockSum.getMin(), blockSum.getMax(), retryCount.incrementAndGet()));
            } else {
                retryCount.set(0);
            }
            log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("redis批量入库异常", e);
            throw e;
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
     * @param redisKeyEnum
     * @return void
     * @date 2021/5/21
     */
    private <T> void statisticsLog(Set<T> data, RedisKeyEnum redisKeyEnum) {
        try {
            if (redisKeyEnum.compareTo(RedisKeyEnum.Block) == 0) {
                LongSummaryStatistics blockSum = ((Set<Block>) data).stream().collect(Collectors.summarizingLong(Block::getNum));
                log.info("redis批量入库成功统计:区块[{}]-[{}]", blockSum.getMin(), blockSum.getMax());
            } else if (redisKeyEnum.compareTo(RedisKeyEnum.Transaction) == 0) {
                log.info("redis批量入库成功统计:交易数[{}]", data.size());
            } else if (redisKeyEnum.compareTo(RedisKeyEnum.Erc20Tx) == 0) {
                log.info("redis批量入库成功统计:erc20交易数[{}]", data.size());
            } else if (redisKeyEnum.compareTo(RedisKeyEnum.Erc721Tx) == 0) {
                log.info("redis批量入库成功统计:erc721交易数[{}]", data.size());
            }
        } catch (Exception e) {
            log.error("redis批量入库统计异常", e);
        }
    }

}
