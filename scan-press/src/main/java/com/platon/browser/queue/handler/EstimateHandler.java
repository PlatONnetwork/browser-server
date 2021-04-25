package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.GasEstimate;
import com.platon.browser.dao.mapper.GasEstimateMapper;
import com.platon.browser.queue.event.EstimateEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class EstimateHandler extends AbstractHandler<EstimateEvent> {

    @Autowired
    private GasEstimateMapper gasEstimateMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.estimate.batch-size}")
    private volatile int batchSize;

    private List<GasEstimate> list = new ArrayList<>();
    @PostConstruct
    private void init(){
        this.setLogger(log);
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (EstimateEvent event, long sequence, boolean endOfBatch ) throws IOException, InterruptedException{
        long startTime = System.currentTimeMillis();
        try {
        	list.addAll(event.getGasEstimates());
            if(list.size()<batchSize) return;
            gasEstimateMapper.batchInsert(list);
            long endTime = System.currentTimeMillis();
            printTps("estimate",list.size(),startTime,endTime);
            list.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}