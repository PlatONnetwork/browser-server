package com.platon.browser.collection.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.common.service.epoch.EpochRetryService;
import com.platon.browser.config.BlockChainConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.platon.bean.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochRetryServiceTest {

    @Mock private BlockChainConfig chainConfig;
    @Mock private AccountService accountService;
    @Mock PlatOnClient platOnClient;
    @Mock SpecialContractApi specialContractApi;
    @Mock List<Node> curVerifiers;
    @Spy private EpochRetryService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "accountService", accountService);
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(target, "specialContractApi", specialContractApi);
        ReflectionTestUtils.setField(target, "curVerifiers", curVerifiers);
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.valueOf(250));
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(10));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(50));
        when(chainConfig.getBlockRewardRate()).thenReturn(BigDecimal.valueOf(0.6));
        when(chainConfig.getStakeRewardRate()).thenReturn(BigDecimal.valueOf(0.4));
        when(chainConfig.getSettlePeriodCountPerIssue()).thenReturn(BigInteger.valueOf(5));
        when(platOnClient.getLatestBlockNumber()).thenReturn(BigInteger.valueOf(501));
        when(accountService.getInciteBalance(any())).thenReturn(BigDecimal.valueOf(10000));
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node());
        nodes.add(new Node());
        nodes.add(new Node());
        nodes.add(new Node());
        when(curVerifiers.size()).thenReturn(4);
        when(specialContractApi.getHistoryValidatorList(any(),any())).thenReturn(nodes);
        when(specialContractApi.getHistoryVerifierList(any(),any())).thenReturn(nodes);
        when(platOnClient.getLatestValidators()).thenReturn(nodes);
        when(platOnClient.getLatestVerifiers()).thenReturn(nodes);
    }

    /**
     * 测试增发周期变更
     */
    @Test
    public void issueEpochChange() throws Exception {
        target.issueChange(BigInteger.valueOf(501));
        assertEquals(6000,target.getInciteAmount4Block().intValue());
        assertEquals(24,target.getBlockReward().intValue());
        assertEquals(4000,target.getInciteAmount4Stake().intValue());
        assertEquals(800,target.getSettleStakeReward().intValue());
        assertEquals(200,target.getStakeReward().intValue());
        verify(target, times(1)).issueChange(any(BigInteger.class));
    }

    /**
     * 测试共识周期变更
     */
    @Test
    public void consensusEpochChange() throws Exception {
        target.consensusChange(BigInteger.valueOf(41));
        verify(target, times(1)).consensusChange(any(BigInteger.class));
    }

    /**
     * 测试结算周期变更
     */
    @Test
    public void settlementEpochChange() throws Exception {
        target.settlementChange(BigInteger.valueOf(321));
        verify(target, times(1)).settlementChange(any(BigInteger.class));
    }
}
