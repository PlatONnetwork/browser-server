package com.platon.browser.service.ppos;

import com.platon.browser.AgentTestBase;
import com.platon.browser.analyzer.ppos.DelegateExitAnalyzer;
import com.platon.browser.analyzer.ppos.DelegateRewardClaimAnalyzer;
import com.platon.browser.analyzer.ppos.ProposalParameterAnalyzer;
import com.platon.browser.analyzer.ppos.StakeCreateAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.DelegateExitResult;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class PPOSServiceTest extends AgentTestBase {
    @Mock
    private StakeCreateAnalyzer stakeCreateAnalyzer;
    @Mock
    private DelegateExitAnalyzer delegateExitAnalyzer;

    @Mock
    private ProposalParameterAnalyzer proposalParameterAnalyzer;
    @Mock
    private DelegateRewardClaimAnalyzer delegateRewardClaimAnalyzer;
    @InjectMocks
    @Spy
    private PPOSService target;

    @Before
    public void setup() throws Exception {
    }

    @Test(expected = Exception.class)
    public void test() {
        CollectionEvent event = new CollectionEvent();
        event.setBlock(blockList.get(0));
        event.setEpochMessage(EpochMessage.newInstance());
        event.getBlock().setDtoTransactions(new ArrayList <>(transactionList));
        Transaction tx = transactionList.get(0);

        tx.setStatus(Transaction.StatusEnum.SUCCESS.getCode());
        tx.setType(Transaction.TypeEnum.TRANSFER.getCode());
        DelegateExitResult der = DelegateExitResult.builder().build();
        when(delegateExitAnalyzer.analyze(any(),any())).thenReturn(der);
        NodeOpt nodeOpt = new NodeOpt();
        when(proposalParameterAnalyzer.analyze(any(),any())).thenReturn(nodeOpt);
        DelegationReward delegationReward = new DelegationReward();
        when(delegateRewardClaimAnalyzer.analyze(any(),any())).thenReturn(delegationReward);
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.STAKE_CREATE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.STAKE_MODIFY.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.STAKE_INCREASE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.STAKE_EXIT.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.DELEGATE_CREATE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.DELEGATE_EXIT.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_TEXT.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_UPGRADE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_CANCEL.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_VOTE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.VERSION_DECLARE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.RESTRICTING_CREATE.getCode());
        target.analyze(event);
        tx.setType(Transaction.TypeEnum.REPORT.getCode());
        target.analyze(event);
        verify(target, times(14)).analyze(any());

        doThrow(new RuntimeException("")).when(stakeCreateAnalyzer).analyze(any(),any());
        target.analyze(event);
    }
}
