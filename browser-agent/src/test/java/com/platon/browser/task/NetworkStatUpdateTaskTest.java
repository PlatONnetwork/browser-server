package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.task.bean.NetworkStatistics;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatUpdateTaskTest extends AgentTestBase {
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private AccountService accountService;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @Spy
    private NetworkStatUpdateTask target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "chainConfig", chainConfig);
        ReflectionTestUtils.setField(target, "accountService", accountService);
        ReflectionTestUtils.setField(target, "statisticBusinessMapper", statisticBusinessMapper);

        NetworkStat networkStat= CollectionNetworkStat.newInstance();
        networkStat.setCurNumber(100L);
        when(networkStatCache.getNetworkStat()).thenReturn(networkStat);
        when(accountService.getInciteBalance(any())).thenReturn(BigDecimal.TEN);
        when(accountService.getLockCabinBalance(any())).thenReturn(BigDecimal.TEN);
        when(accountService.getStakingBalance(any())).thenReturn(BigDecimal.TEN);

        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(chainConfig.getFoundationSubsidies()).thenReturn(subsidies);
        when(chainConfig.getInitIssueAmount()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getAddIssueRate()).thenReturn(BigDecimal.valueOf(0.025));
        when(statisticBusinessMapper.getNetworkStatisticsFromAddress()).thenReturn(12);
        when(statisticBusinessMapper.getNetworkStatisticsFromProposal()).thenReturn(13);
    }

    @Test
    public void test() throws BlockNumberException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);

        NetworkStatistics networkStatistics = NetworkStatistics.builder()
                .stakingValue(BigDecimal.TEN)
                .totalValue(BigDecimal.TEN)
                .build();
        when(statisticBusinessMapper.getNetworkStatisticsFromNode()).thenReturn(networkStatistics);
        target.cron();
        networkStatistics.setStakingValue(null);
        networkStatistics.setTotalValue(null);
        when(statisticBusinessMapper.getNetworkStatisticsFromNode()).thenReturn(networkStatistics);
        target.cron();

        verify(target, times(2)).cron();

        doThrow(new RuntimeException("")).when(networkStatCache).getNetworkStat();
        target.cron();
    }
}
