package com.platon.browser.persistence.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.service.elasticsearch.EsImportService;
import com.platon.browser.common.service.redis.RedisImportService;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.persistence.queue.event.PersistenceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class PersistenceEventHandler implements EventHandler<PersistenceEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Autowired
    private NetworkStatCache networkStatCache;

    @Value("${disruptor.queue.persistence.batch-size}")
    private int batchSize;

    private Set<Block> blockStage = new HashSet<>();
    private Set<Transaction> transactionStage = new HashSet<>();
    private Set<NodeOpt> nodeOptStage = new HashSet<>();

    private Long preBlockNum=0L;
    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "PersistenceEventHandler")
    public void onEvent(PersistenceEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        log.debug("PersistenceEvent处理:{}(event(block({}),transactions({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlock().getNum(),event.getTransactions().size(),sequence,endOfBatch);
        if(preBlockNum!=0L&&(event.getBlock().getNum()-preBlockNum!=1)) throw new AssertionError();
        try {
            blockStage.add(event.getBlock());
            transactionStage.addAll(event.getTransactions());
            nodeOptStage.addAll(event.getNodeOpts());

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(blockStage.size()<batchSize) {
                preBlockNum=event.getBlock().getNum();
                return;
            }

            // 入库ES TODO: 入库委托记录和节点操作记录到ES
            esImportService.batchImport(blockStage,transactionStage,nodeOptStage);
            // 入库Redis TODO: 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            statistics.add(networkStatCache.getNetworkStat());
            redisImportService.batchImport(blockStage,transactionStage,statistics);
            blockStage.clear();
            transactionStage.clear();

            preBlockNum=event.getBlock().getNum();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}