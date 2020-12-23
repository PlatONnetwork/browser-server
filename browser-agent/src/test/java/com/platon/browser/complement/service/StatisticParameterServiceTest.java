package com.platon.browser.complement.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.statistic.StatisticsAddressConverter;
import com.platon.browser.complement.converter.statistic.StatisticsNetworkConverter;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class StatisticParameterServiceTest extends AgentTestBase {
    @Mock
    private AddressCache addressCache;
    @Mock
    private StatisticsNetworkConverter statisticsNetworkConverter;
    @Mock
    private StatisticsAddressConverter statisticsAddressConverter;
    @InjectMocks
    @Spy
    private StatisticParameterService target;

    @Before
    public void setup() throws Exception {
        when(addressCache.getAll()).thenReturn(new ArrayList<>(addressList));
    }

    @Test
    public void test() throws Exception {
        Block block = blockList.get(0);
        EpochMessage epochMessage=EpochMessage.newInstance();
        CollectionEvent event = new CollectionEvent();
        event.setBlock(blockList.get(0));
        event.setEpochMessage(EpochMessage.newInstance());
        event.setTransactions(new ArrayList <>(transactionList));
        target.getParameters(event);
        verify(target, times(1)).getParameters(any());
    }
}
