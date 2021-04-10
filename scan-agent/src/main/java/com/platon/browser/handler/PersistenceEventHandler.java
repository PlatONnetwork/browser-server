package com.platon.browser.handler;

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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "PersistenceEventHandler")
    public void onEvent(PersistenceEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        log.debug("PersistenceEvent处理:{}(event(block({}),transactions({}),nodeOpts({}),delegateRewards({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(), event.getBlock().getNum(), event.getTransactions().size(),
                event.getNodeOpts().size(), event.getDelegationRewards().size(), sequence, endOfBatch);
        try {
            blockStage.add(event.getBlock());
            transactionStage.addAll(event.getTransactions());
            nodeOptStage.addAll(event.getNodeOpts());
            delegationRewardStage.addAll(event.getDelegationRewards());

            // 把区块的交易列表属性置为null,防止把交易信息存储到区块信息中
            event.getBlock().setTransactions(null);

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(blockStage.size()<disruptorConfig.getPersistenceBatchSize()) {
                maxBlockNumber=event.getBlock().getNum();
                return;
            }

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(blockStage, transactionStage, nodeOptStage, delegationRewardStage);

            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            statistics.add(networkStatCache.getNetworkStat());
            redisImportService.batchImport(blockStage,transactionStage,statistics);
            blockStage.clear();
            transactionStage.clear();
            nodeOptStage.clear();
            delegationRewardStage.clear();

            // 查询序号最大的一条交易备份记录, 通知备份数据删除任务删除记录
            Long txMaxId = 0L;
            List<Transaction> transactions = event.getTransactions();
            if(!transactions.isEmpty()) {
                for (Transaction tx : transactions) {
                    if(tx.getId()>txMaxId) txMaxId=tx.getId();
                }
            }
            BakDataDeleteUtil.updateTxBakMaxId(txMaxId);

            // 查询序号最大的一条操作记录, 通知日志备份数据删除任务删除记录
            List<NodeOpt> nOptBaks = event.getNodeOpts();
            Long nOptMaxId = 0L;
            if(!nOptBaks.isEmpty()) {
                for (NodeOpt no : nOptBaks) {
                    if(no.getId()>nOptMaxId) nOptMaxId=no.getId();
                }
                nOptMaxId=nOptBaks.get(0).getId();
            }
            BakDataDeleteUtil.updateNOptBakMaxId(nOptMaxId);

            maxBlockNumber=event.getBlock().getNum();
            // 释放对象引用
            event.releaseRef();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}