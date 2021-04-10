package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.queue.event.VoteEvent;
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
public class VoteHandler extends AbstractHandler<VoteEvent> {

    @Autowired
    private VoteMapper voteMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.vote.batch-size}")
    private volatile int batchSize;

    private List<Vote> stage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (VoteEvent event, long sequence, boolean endOfBatch ) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getVoteList());
            if(stage.size()<batchSize) return;
            voteMapper.batchInsert(stage);
            long endTime = System.currentTimeMillis();
            printTps("投票",stage.size(),startTime,endTime);
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}