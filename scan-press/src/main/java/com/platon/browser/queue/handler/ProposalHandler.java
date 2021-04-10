package com.platon.browser.queue.handler;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.queue.event.ProposalEvent;
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
public class ProposalHandler extends AbstractHandler<ProposalEvent> {

    @Autowired
    private ProposalMapper proposalMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.proposal.batch-size}")
    private volatile int batchSize;

    private List<Proposal> stage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (ProposalEvent event, long sequence, boolean endOfBatch ) {
        long startTime = System.currentTimeMillis();
        try {
            stage.addAll(event.getProposalList());
            if(stage.size()<batchSize) return;
            proposalMapper.batchInsert(stage);
            long endTime = System.currentTimeMillis();
            printTps("提案",stage.size(),startTime,endTime);
            stage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}