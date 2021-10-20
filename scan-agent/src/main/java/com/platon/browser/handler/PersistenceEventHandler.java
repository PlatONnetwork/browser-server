package com.platon.browser.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.bean.PersistenceEvent;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.DisruptorConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import com.platon.browser.utils.BakDataDeleteUtil;
import com.platon.browser.utils.CommonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
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

    private Set<NodeOpt> nodeOptStage = new HashSet<>();

    private Set<DelegationReward> delegationRewardStage = new HashSet<>();

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, label = "PersistenceEventHandler")
    public void onEvent(PersistenceEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        surroundExec(event, sequence, endOfBatch);
    }

    private void surroundExec(PersistenceEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(PersistenceEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        try {
            log.info("当前区块[{}]有[{}]笔交易,有[{}]笔节点操作,有[{}]笔委托奖励",
                     event.getBlock().getNum(),
                     CommonUtil.ofNullable(() -> event.getTransactions().size()).orElse(0),
                     CommonUtil.ofNullable(() -> event.getNodeOpts().size()).orElse(0),
                     CommonUtil.ofNullable(() -> event.getDelegationRewards().size()).orElse(0));
            blockStage.add(event.getBlock());
            transactionStage.addAll(event.getTransactions());
            nodeOptStage.addAll(event.getNodeOpts());
            delegationRewardStage.addAll(event.getDelegationRewards());

            List<Long> blockNums = CollUtil.newArrayList();
            if (retryCount.incrementAndGet() > 1) {
                if (CollUtil.isNotEmpty(blockStage)) {
                    blockNums = blockStage.stream().map(Block::getNum).sorted().collect(Collectors.toList());
                }
                // ES的id是用hash做key，重复入库会覆盖
                // redis是上游做了去重，如果入库时重试，是没有实现去重机制的
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
                log.info("相关区块[{}]达到入库标准", JSONUtil.toJsonStr(blockNums));
            }

            statisticsLog();

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(blockStage, transactionStage, nodeOptStage, delegationRewardStage);

            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            statistics.add(networkStatCache.getNetworkStat());
            redisImportService.batchImport(blockStage, transactionStage, statistics);
            blockStage.clear();
            transactionStage.clear();
            nodeOptStage.clear();
            delegationRewardStage.clear();

            // 查询序号最大的一条交易备份记录, 通知备份数据删除任务删除记录
            Long txMaxId = 0L;
            List<Transaction> transactions = event.getTransactions();
            if (!transactions.isEmpty()) {
                for (Transaction tx : transactions) {
                    if (tx.getId() > txMaxId) txMaxId = tx.getId();
                }
            }
            BakDataDeleteUtil.updateTxBakMaxId(txMaxId);

            // 查询序号最大的一条操作记录, 通知日志备份数据删除任务删除记录
            List<NodeOpt> nOptBaks = event.getNodeOpts();
            Long nOptMaxId = 0L;
            if (!nOptBaks.isEmpty()) {
                for (NodeOpt no : nOptBaks) {
                    if (no.getId() > nOptMaxId) nOptMaxId = no.getId();
                }
                nOptMaxId = nOptBaks.get(0).getId();
            }
            BakDataDeleteUtil.updateNOptBakMaxId(nOptMaxId);

            maxBlockNumber = event.getBlock().getNum();
            // 释放对象引用
            event.releaseRef();
            retryCount.set(0);
        } catch (Exception e) {
            log.error("", e);
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