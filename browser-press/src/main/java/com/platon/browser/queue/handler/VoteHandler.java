package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
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
public class VoteHandler extends AbstractHandler implements EventHandler<VoteEvent> {

    @Autowired
    private VoteMapper voteMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.vote.batch-size}")
    private volatile int batchSize;

    private List<Vote> voteStage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (VoteEvent event, long sequence, boolean endOfBatch ) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            voteStage.addAll(event.getVoteList());
            if(voteStage.size()<batchSize) return;
            voteMapper.batchInsert(voteStage);
            long endTime = System.currentTimeMillis();
            printTps("投票",voteStage.size(),startTime,endTime);
            voteStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}