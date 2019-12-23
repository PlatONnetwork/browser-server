package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.queue.event.NodeOptEvent;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class NodeOptHandler  extends AbstractHandler implements EventHandler<NodeOptEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Setter
    @Getter
    @Value("${disruptor.queue.nodeopt.batch-size}")
    private volatile int batchSize;

    private Set<NodeOpt> nodeOptStage = new HashSet<>();

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(NodeOptEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        log.debug("NodeOptEvent处理:{}(event(nodeOptList({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getNodeOptList().size());
        try {
            nodeOptStage.addAll(event.getNodeOptList());

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(nodeOptStage.size()<batchSize) {
                return;
            }

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(Collections.emptySet(), Collections.emptySet(),nodeOptStage);

            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            redisImportService.batchImport(Collections.emptySet(),Collections.emptySet(),statistics);
            nodeOptStage.clear();

            long endTime = System.currentTimeMillis();
            printTps("日志",nodeOptStage.size(),startTime,endTime);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}