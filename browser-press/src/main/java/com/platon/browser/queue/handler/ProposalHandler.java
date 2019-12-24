package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.queue.event.DelegationEvent;
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
public class ProposalHandler extends AbstractHandler implements EventHandler<ProposalEvent> {

    @Autowired
    private ProposalMapper proposalMapper;

    @Setter
    @Getter
    @Value("${disruptor.queue.proposal.batch-size}")
    private volatile int batchSize;

    private List<Proposal> proposalStage = new ArrayList<>();
    @PostConstruct
    private void init(){this.setLogger(log);}

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent (ProposalEvent event, long sequence, boolean endOfBatch ) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            proposalStage.addAll(event.getProposalList());
            if(proposalStage.size()<batchSize) return;
            proposalMapper.batchInsert(proposalStage);
            long endTime = System.currentTimeMillis();
            printTps("提案",proposalStage.size(),startTime,endTime);
            proposalStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}