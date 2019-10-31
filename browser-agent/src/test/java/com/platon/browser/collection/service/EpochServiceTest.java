package com.platon.browser.collection.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.collection.service.epoch.EpochService;
import com.platon.browser.config.BlockChainConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochServiceTest {

    @Mock private BlockChainConfig chainConfig;
    @Mock private PlatOnClient platOnClient;
    @Mock private SpecialContractApi specialContractApi;
    @Spy private EpochService epochService;

    @Before
    public void setup() throws InterruptedException {
        ReflectionTestUtils.setField(epochService, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(epochService, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(epochService, "specialContractApi", specialContractApi);
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate() throws InterruptedException {
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(10));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(50));
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.valueOf(250));
        // 测试区块0
        epochService.update(BigInteger.valueOf(0));
        assertEquals(0,epochService.getConsensusEpochRound());
        assertEquals(0,epochService.getSettleEpochRound());
        assertEquals(0,epochService.getIssueEpochRound());
        // 测试区块5
        epochService.update(BigInteger.valueOf(5));
        assertEquals(1,epochService.getConsensusEpochRound());
        assertEquals(1,epochService.getSettleEpochRound());
        assertEquals(1,epochService.getIssueEpochRound());
        // 测试区块10
        epochService.update(BigInteger.valueOf(10));
        assertEquals(1,epochService.getConsensusEpochRound());
        assertEquals(1,epochService.getSettleEpochRound());
        assertEquals(1,epochService.getIssueEpochRound());
        // 测试区块251
        epochService.update(BigInteger.valueOf(251));
        assertEquals(26,epochService.getConsensusEpochRound());
        assertEquals(6,epochService.getSettleEpochRound());
        assertEquals(2,epochService.getIssueEpochRound());
    }

}
