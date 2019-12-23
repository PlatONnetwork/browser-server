package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.NodeEvent;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.queue.publisher.AddressPublisher;
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
import java.io.IOException;
import java.util.*;

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class NodeHandler extends AbstractHandler implements EventHandler<NodeEvent> {

    @Autowired
    private NodeMapper nodeMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    private List<Node> nodeStage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent ( NodeEvent event, long sequence, boolean endOfBatch ) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            nodeStage.addAll(event.getNodeList());
            if(nodeStage.size()<batchSize) return;
            nodeMapper.batchInsert(nodeStage);
            long endTime = System.currentTimeMillis();
            printTps("节点",nodeStage.size(),startTime,endTime);
            nodeStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}