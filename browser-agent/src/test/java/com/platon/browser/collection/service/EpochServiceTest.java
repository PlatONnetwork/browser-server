package com.platon.browser.collection.service;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.collection.service.epoch.EpochService;
import com.platon.browser.common.service.AccountService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.exception.BlockNumberException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EpochServiceTest {

    @Mock private BlockChainConfig chainConfig;
    @Mock private PlatOnClient platOnClient;
    @Mock private SpecialContractApi specialContractApi;
    @Mock private AccountService accountService;
    @Spy private EpochService epochService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(epochService, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(epochService, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(epochService, "specialContractApi", specialContractApi);
        ReflectionTestUtils.setField(epochService, "accountService", accountService);
    }

    /**
     * 测试更新
     */
    @Test
    public void testUpdate() throws InterruptedException, BlockNumberException {
        when(chainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.valueOf(10));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(50));
        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.valueOf(250));
        when(accountService.getInciteBalance(any())).thenReturn(BigInteger.valueOf(10000));
        when(chainConfig.getBlockRewardRate()).thenReturn(BigDecimal.valueOf(0.6));
        when(chainConfig.getStakeRewardRate()).thenReturn(BigDecimal.valueOf(0.4));
        when(chainConfig.getSettlePeriodCountPerIssue()).thenReturn(BigInteger.valueOf(5));
        // 测试区块-1
        epochService.update(BigInteger.valueOf(-100));
        assertEquals(0,epochService.getConsensusEpochRound().intValue());
        assertEquals(0,epochService.getSettleEpochRound().intValue());
        assertEquals(0,epochService.getIssueEpochRound().intValue());
        // 测试区块0
        epochService.update(BigInteger.valueOf(0));
        assertEquals(0,epochService.getConsensusEpochRound().intValue());
        assertEquals(0,epochService.getSettleEpochRound().intValue());
        assertEquals(0,epochService.getIssueEpochRound().intValue());
        // 测试区块1
        epochService.update(BigInteger.valueOf(1));
        assertEquals(1,epochService.getConsensusEpochRound().intValue());
        assertEquals(1,epochService.getSettleEpochRound().intValue());
        assertEquals(1,epochService.getIssueEpochRound().intValue());
        // 测试区块5
        epochService.update(BigInteger.valueOf(5));
        assertEquals(1,epochService.getConsensusEpochRound().intValue());
        assertEquals(1,epochService.getSettleEpochRound().intValue());
        assertEquals(1,epochService.getIssueEpochRound().intValue());
        // 测试区块10
        epochService.update(BigInteger.valueOf(10));
        assertEquals(1,epochService.getConsensusEpochRound().intValue());
        assertEquals(1,epochService.getSettleEpochRound().intValue());
        assertEquals(1,epochService.getIssueEpochRound().intValue());
        // 测试区块251
        epochService.update(BigInteger.valueOf(251));
        assertEquals(26,epochService.getConsensusEpochRound().intValue());
        assertEquals(6,epochService.getSettleEpochRound().intValue());
        assertEquals(2,epochService.getIssueEpochRound().intValue());
        // 增发周期切换
        epochService.update(BigInteger.valueOf(501));
        assertEquals(51,epochService.getConsensusEpochRound().intValue());
        assertEquals(11,epochService.getSettleEpochRound().intValue());
        assertEquals(3,epochService.getIssueEpochRound().intValue());
        // 验证各金额计算结果
//        assertEquals(6000,epochService.getInciteAmount4Block().intValue());
//        assertEquals(4000,epochService.getInciteAmount4Stake().intValue());
//        assertEquals(24,epochService.getBlockReward().intValue());
//        assertEquals(800,epochService.getSettleStakeReward().intValue());
    }

}
