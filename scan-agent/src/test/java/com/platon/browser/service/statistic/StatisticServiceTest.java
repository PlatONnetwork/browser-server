package com.platon.browser.service.statistic;

import com.platon.browser.AgentTestBase;
import com.platon.browser.analyzer.statistic.StatisticsAddressAnalyzer;
import com.platon.browser.analyzer.statistic.StatisticsNetworkAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.elasticsearch.dto.Block;
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
public class StatisticServiceTest extends AgentTestBase {
    @Mock
    private NewAddressCache newAddressCache;
    @Mock
    private StatisticsNetworkAnalyzer statisticsNetworkAnalyzer;
    @Mock
    private StatisticsAddressAnalyzer statisticsAddressAnalyzer;
    @InjectMocks
    @Spy
    private StatisticService target;

    @Before
    public void setup() throws Exception {
        when(newAddressCache.listNewAddressInBlockCtx()).thenReturn(new ArrayList<>(addressList));
    }

    @Test
    public void test() throws Exception {
        Block block = blockList.get(0);
        EpochMessage epochMessage=EpochMessage.newInstance();
        CollectionEvent event = new CollectionEvent();
        event.setBlock(blockList.get(0));
        event.setEpochMessage(EpochMessage.newInstance());
        event.getBlock().setDtoTransactions(new ArrayList <>(transactionList));
        target.analyze(event);
        verify(target, times(1)).analyze(any());
    }
}
