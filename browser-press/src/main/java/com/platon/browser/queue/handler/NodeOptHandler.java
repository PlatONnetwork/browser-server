package com.platon.browser.queue.handler;

import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.queue.event.NodeOptEvent;
import com.platon.browser.service.elasticsearch.EsNodeOptService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class NodeOptHandler  extends AbstractHandler<NodeOptEvent> {

    @Autowired
    private EsNodeOptService esNodeOptService;

    @Setter
    @Getter
    @Value("${disruptor.queue.nodeopt.batch-size}")
    private volatile int batchSize;

    private StageCache<NodeOpt> stage = new StageCache<>();
    @PostConstruct
    private void init(){
        stage.setBatchSize(batchSize);
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(NodeOptEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Set<NodeOpt> cache = stage.getData();
        try {
            cache.addAll(event.getNodeOptList());
            if(cache.size()<batchSize) return;
            esNodeOptService.save(stage);
            long endTime = System.currentTimeMillis();
            printTps("日志",cache.size(),startTime,endTime);
            cache.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}