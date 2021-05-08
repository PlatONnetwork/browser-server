package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.AgentTestData;
import com.platon.browser.bean.CollectionNetworkStat;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.service.account.AccountService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.task.bean.NetworkStatistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.platon.browser.AgentTestBase.subsidies;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatUpdateTaskTest extends AgentTestData {
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private BlockChainConfig chainConfig;
    @Mock
    private AccountService accountService;
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @InjectMocks
    @Spy
    private NetworkStatUpdateTask target;

    @Before
    public void setup() throws Exception {
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

        NetworkStatistics networkStatistics = new NetworkStatistics();
        networkStatistics.setStakingValue(BigDecimal.TEN);
        networkStatistics.setTotalValue(BigDecimal.TEN);
        when(statisticBusinessMapper.getNetworkStatisticsFromNode()).thenReturn(networkStatistics);
        target.networkStatUpdate();
        networkStatistics.setStakingValue(null);
        networkStatistics.setTotalValue(null);
        when(statisticBusinessMapper.getNetworkStatisticsFromNode()).thenReturn(networkStatistics);
        target.networkStatUpdate();

        verify(target, times(2)).networkStatUpdate();

        doThrow(new RuntimeException("")).when(networkStatCache).getNetworkStat();
        target.networkStatUpdate();
    }
}
