package com.platon.browser.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.bean.CommonConstant;
import com.platon.browser.bean.PersistenceEvent;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.DisruptorConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import com.platon.browser.utils.CommonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class PersistenceEventHandler implements EventHandler<PersistenceEvent> {

    @Resource
    private EsImportService esImportService;

    @Resource
    private RedisImportService redisImportService;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private DisruptorConfig disruptorConfig;

    // 处理的最大区块号
    @Getter
    private volatile long maxBlockNumber;

    private Set<Block> blockStage = new HashSet<>();

    private Set<Transaction> transactionStage = new HashSet<>();

    private Set<DelegationReward> delegationRewardStage = new HashSet<>();

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, label = "PersistenceEventHandler")
    public void onEvent(PersistenceEvent event, long sequence, boolean endOfBatch) throws Exception {
        surroundExec(event, sequence, endOfBatch);
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public void recover(Exception e) {
        retryCount.set(0);
        log.error("重试完成还是业务失败，请联系管理员处理");
    }

    private void surroundExec(PersistenceEvent event, long sequence, boolean endOfBatch) throws Exception {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(PersistenceEvent event, long sequence, boolean endOfBatch) throws Exception {
        try {
            log.info("当前区块[{}]有[{}]笔交易,有[{}]笔节点操作,有[{}]笔委托奖励",
                     event.getBlock().getNum(),
                     CommonUtil.ofNullable(() -> event.getTransactions().size()).orElse(0),
                     CommonUtil.ofNullable(() -> event.getNodeOpts().size()).orElse(0),
                     CommonUtil.ofNullable(() -> event.getDelegationRewards().size()).orElse(0));
            blockStage.add(event.getBlock());
            transactionStage.addAll(event.getTransactions());
            delegationRewardStage.addAll(event.getDelegationRewards());

            List<Long> blockNums = CollUtil.newArrayList();
            if (retryCount.incrementAndGet() > 1) {
                if (CollUtil.isNotEmpty(blockStage)) {
                    blockNums = blockStage.stream().map(Block::getNum).sorted().collect(Collectors.toList());
                }
                // ES的id是用hash做key，重复入库会覆盖
                log.error("相关区块[{}]的数据重复入库，可能引起数据重复入库，重试次数[{}]", JSONUtil.toJsonStr(blockNums), retryCount.get());
            }

            // 把区块的交易列表属性置为null,防止把交易信息存储到区块信息中
            event.getBlock().setTransactions(null);

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if (blockStage.size() < disruptorConfig.getPersistenceBatchSize()) {
                maxBlockNumber = event.getBlock().getNum();
                retryCount.set(0);
                return;
            } else {
                blockNums = blockStage.stream().map(Block::getNum).sorted().collect(Collectors.toList());
                log.info("相关区块[{}]达到入库标准", JSONUtil.toJsonStr(blockNums));
            }

            statisticsLog();

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(blockStage, transactionStage, delegationRewardStage);

            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            statistics.add(networkStatCache.getNetworkStat());
            redisImportService.batchImport(blockStage, transactionStage, statistics);
            blockStage.clear();
            transactionStage.clear();
            delegationRewardStage.clear();

            maxBlockNumber = event.getBlock().getNum();
            // 释放对象引用
            event.releaseRef();
            retryCount.set(0);
        } catch (Exception e) {
            log.error("数据入库异常", e);
            throw e;
        }
    }

    /**
     * 打印统计信息
     *
     * @param
     * @return void
     * @date 2021/4/24
     */
    private void statisticsLog() {
        try {
            Map<Object, List<Transaction>> map = transactionStage.stream().collect(Collectors.groupingBy(Transaction::getNum));
            if (CollUtil.isNotEmpty(transactionStage)) {
                map.forEach((blockNum, transactions) -> {
                    IntSummaryStatistics erc20Size = transactions.stream().collect(Collectors.summarizingInt(transaction -> transaction.getErc20TxList().size()));
                    IntSummaryStatistics erc721Size = transactions.stream().collect(Collectors.summarizingInt(transaction -> transaction.getErc721TxList().size()));
                    IntSummaryStatistics transferTxSize = transactions.stream().collect(Collectors.summarizingInt(transaction -> transaction.getTransferTxList().size()));
                    IntSummaryStatistics pposTxSize = transactions.stream().collect(Collectors.summarizingInt(transaction -> transaction.getPposTxList().size()));
                    IntSummaryStatistics virtualTransactionSize = transactions.stream().collect(Collectors.summarizingInt(transaction -> transaction.getVirtualTransactions().size()));
                    log.info("准备入库redis和ES:当前块高为[{}],交易数为[{}],erc20交易数为[{}],erc721交易数为[{}],内部转账交易数为[{}],PPOS调用交易数为[{}],虚拟交易数为[{}]",
                             blockNum,
                             CommonUtil.ofNullable(() -> transactions.size()).orElse(0),
                             erc20Size.getSum(),
                             erc721Size.getSum(),
                             transferTxSize.getSum(),
                             pposTxSize.getSum(),
                             virtualTransactionSize.getSum());
                });
            }
        } catch (Exception e) {
            log.error("打印统计信息异常", e);
        }
    }

}