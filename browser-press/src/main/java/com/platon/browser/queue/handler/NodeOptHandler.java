package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.queue.event.NodeOptEvent;
import com.platon.browser.service.DataGenService;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class NodeOptHandler  extends AbstractHandler<NodeOptEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Autowired
    private DataGenService dataGenService;

    @Setter
    @Getter
    @Value("${disruptor.queue.nodeopt.batch-size}")
    private volatile int batchSize;

    private Set<NodeOpt> stage = new HashSet<>();

    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(NodeOptEvent event, long sequence, boolean endOfBatch) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getNodeOptList());
            if(stage.size()<batchSize) return;
            esImportService.batchImport(Collections.emptySet(), Collections.emptySet(),stage);
            Set<NetworkStat> statistics = new HashSet<>();
            statistics.add(dataGenService.getNetworkStat());
            redisImportService.batchImport(Collections.emptySet(),Collections.emptySet(),statistics);
            long endTime = System.currentTimeMillis();
            printTps("日志",stage.size(),startTime,endTime);
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}