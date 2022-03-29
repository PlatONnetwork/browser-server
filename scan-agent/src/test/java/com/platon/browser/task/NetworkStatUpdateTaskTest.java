package com.platon.browser.task;

import com.platon.browser.AgentTestData;
import com.platon.browser.bean.CollectionNetworkStat;
import com.platon.browser.bean.CountBalance;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.CustomInternalAddressMapper;
import com.platon.browser.dao.custommapper.CustomRpPlanMapper;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.service.account.AccountService;
import com.platon.browser.task.bean.NetworkStatistics;
import com.platon.browser.utils.AppStatusUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

    @Mock
    private CustomInternalAddressMapper customInternalAddressMapper;
    @Mock
    private CustomRpPlanMapper customRpPlanMapper;
    @Before
    public void setup() throws Exception {
        NetworkStat networkStat = CollectionNetworkStat.newInstance();
        networkStat.setCurNumber(100L);
        when(networkStatCache.getNetworkStat()).thenReturn(networkStat);
        when(accountService.getInciteBalance(any())).thenReturn(BigDecimal.TEN);
        when(accountService.getLockCabinBalance(any())).thenReturn(BigDecimal.TEN);
        when(accountService.getStakingBalance(any())).thenReturn(BigDecimal.TEN);

        when(chainConfig.getAddIssuePeriodBlockCount()).thenReturn(BigInteger.TEN);
        when(chainConfig.getFoundationSubsidies()).thenReturn(subsidies);
        when(chainConfig.getInitIssueAmount()).thenReturn(BigDecimal.TEN);
        when(chainConfig.getAddIssueRate()).thenReturn(BigDecimal.valueOf(0.025));
        when(chainConfig.getSettlePeriodBlockCount()).thenReturn(BigInteger.valueOf(10750L));
        when(statisticBusinessMapper.getNetworkStatisticsFromAddress()).thenReturn(12);
        when(statisticBusinessMapper.getNetworkStatisticsFromProposal()).thenReturn(13);
        List<CountBalance> list = new ArrayList<>();
        CountBalance countBalance1 = new CountBalance();
        countBalance1.setType(0);
        countBalance1.setLocked(BigDecimal.TEN);
        countBalance1.setFree(BigDecimal.TEN);
        CountBalance countBalance2 = new CountBalance();
        countBalance2.setType(3);
        countBalance2.setLocked(BigDecimal.TEN);
        countBalance2.setFree(BigDecimal.TEN);
        CountBalance countBalance3 = new CountBalance();
        countBalance3.setType(6);
        countBalance3.setLocked(BigDecimal.TEN);
        countBalance3.setFree(BigDecimal.TEN);
        list.add(countBalance1);
        list.add(countBalance2);
        list.add(countBalance3);
        when(customInternalAddressMapper.countBalance()).thenReturn(list);
        when(customRpPlanMapper.getRPNotExpiredValue(any(),any())).thenReturn(BigDecimal.TEN);
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

//        doThrow(new RuntimeException("")).when(networkStatCache).getNetworkStat();
//        target.networkStatUpdate();
    }

}
