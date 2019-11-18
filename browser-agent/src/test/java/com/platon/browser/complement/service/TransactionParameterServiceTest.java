package com.platon.browser.complement.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.delegate.DelegateCreateConverter;
import com.platon.browser.complement.converter.delegate.DelegateExitConverter;
import com.platon.browser.complement.converter.proposal.*;
import com.platon.browser.complement.converter.restricting.RestrictingCreateConverter;
import com.platon.browser.complement.converter.slash.ReportConverter;
import com.platon.browser.complement.converter.stake.StakeCreateConverter;
import com.platon.browser.complement.converter.stake.StakeExitConverter;
import com.platon.browser.complement.converter.stake.StakeIncreaseConverter;
import com.platon.browser.complement.converter.stake.StakeModifyConverter;
import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionParameterServiceTest extends AgentTestBase {
    @Mock
    private StakeCreateConverter stakeCreateConverter;
    @Mock
    private StakeModifyConverter stakeModifyConverter;
    @Mock
    private StakeIncreaseConverter stakeIncreaseConverter;
    @Mock
    private StakeExitConverter stakeExitConverter;
    @Mock
    private ReportConverter reportConverter;
    @Mock
    private DelegateCreateConverter delegateCreateConverter;
    @Mock
    private DelegateExitConverter delegateExitConverter;
    @Mock
    private ProposalTextConverter proposalTextConverter;
    @Mock
    private ProposalUpgradeConverter proposalUpgradeConverter;
    @Mock
    private ProposalCancelConverter proposalCancelConverter;
    @Mock
    private ProposalVoteConverter proposalVoteConverter;
    @Mock
    private VersionDeclareConverter proposalVersionConverter;
    @Mock
    private RestrictingCreateConverter restrictingCreateConverter;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private AddressCache addressCache;

    @Spy
    private TransactionParameterService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "stakeCreateConverter", stakeCreateConverter);
        ReflectionTestUtils.setField(target, "stakeModifyConverter", stakeModifyConverter);
        ReflectionTestUtils.setField(target, "stakeIncreaseConverter", stakeIncreaseConverter);
        ReflectionTestUtils.setField(target, "stakeExitConverter", stakeExitConverter);
        ReflectionTestUtils.setField(target, "reportConverter", reportConverter);
        ReflectionTestUtils.setField(target, "delegateCreateConverter", delegateCreateConverter);
        ReflectionTestUtils.setField(target, "delegateExitConverter", delegateExitConverter);
        ReflectionTestUtils.setField(target, "proposalTextConverter", proposalTextConverter);
        ReflectionTestUtils.setField(target, "proposalUpgradeConverter", proposalUpgradeConverter);
        ReflectionTestUtils.setField(target, "proposalCancelConverter", proposalCancelConverter);
        ReflectionTestUtils.setField(target, "proposalVoteConverter", proposalVoteConverter);
        ReflectionTestUtils.setField(target, "proposalVersionConverter", proposalVersionConverter);
        ReflectionTestUtils.setField(target, "restrictingCreateConverter", restrictingCreateConverter);
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "addressCache", addressCache);
    }

    @Test(expected = Exception.class)
    public void test() throws Exception {
        Block block = blockList.get(0);
        EpochMessage epochMessage=EpochMessage.newInstance();
        CollectionEvent event = CollectionEvent.builder()
                .block(block)
                .transactions(new ArrayList<>(transactionList))
                .epochMessage(epochMessage)
                .build();
        Transaction tx = transactionList.get(0);

        tx.setStatus(Transaction.StatusEnum.SUCCESS.getCode());
        tx.setType(Transaction.TypeEnum.TRANSFER.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.STAKE_CREATE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.STAKE_MODIFY.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.STAKE_INCREASE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.STAKE_EXIT.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.DELEGATE_CREATE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.DELEGATE_EXIT.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_TEXT.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_UPGRADE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_CANCEL.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.PROPOSAL_VOTE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.VERSION_DECLARE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.RESTRICTING_CREATE.getCode());
        target.getParameters(event);
        tx.setType(Transaction.TypeEnum.REPORT.getCode());
        target.getParameters(event);
        verify(target, times(14)).getParameters(any());

        doThrow(new RuntimeException("")).when(stakeCreateConverter).convert(any(),any());
        target.getParameters(event);
    }
}
