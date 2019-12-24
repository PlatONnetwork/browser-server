package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Slash;
import com.platon.browser.dao.mapper.SlashMapper;
import com.platon.browser.queue.event.SlashEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 节点事件处理器
 */
@Slf4j
@Component
public class SlashHandler extends AbstractHandler implements EventHandler<SlashEvent> {

    @Autowired
    private SlashMapper slashMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.slash.batch-size}")
    private volatile int batchSize;

    private List<Slash> slashStage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (SlashEvent event, long sequence, boolean endOfBatch ) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            slashStage.addAll(event.getSlashList());
            if(slashStage.size()<batchSize) return;
            slashMapper.batchInsert(slashStage);
            long endTime = System.currentTimeMillis();
            printTps("惩罚",slashStage.size(),startTime,endTime);
            slashStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}