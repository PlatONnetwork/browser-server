package com.platon.browser.complement.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.queue.complement.publisher.ComplementEventPublisher;
import com.platon.browser.complement.service.BlockParameterService;
import com.platon.browser.complement.service.StatisticParameterService;
import com.platon.browser.complement.service.TransactionParameterService;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.TxBakMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class CollectionEventHandlerTest extends AgentTestBase {
    @Mock
    private TransactionParameterService transactionParameterService;
    @Mock
    private BlockParameterService blockParameterService;
    @Mock
    private StatisticParameterService statisticParameterService;
    @Mock
    private ComplementEventPublisher complementEventPublisher;
    @Mock
    private NetworkStatCache networkStatCache;
    @Mock
    private NOptBakMapper nOptBakMapper;
    @Mock
    private TxBakMapper txBakMapper;

    @Spy
    private CollectionEventHandler target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "txDeleteBatchCount", 100);
        ReflectionTestUtils.setField(target, "optDeleteBatchCount", 100);
        ReflectionTestUtils.setField(target, "transactionParameterService", transactionParameterService);
        ReflectionTestUtils.setField(target, "blockParameterService", blockParameterService);
        ReflectionTestUtils.setField(target, "statisticParameterService", statisticParameterService);
        ReflectionTestUtils.setField(target, "complementEventPublisher", complementEventPublisher);
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "networkStatCache", networkStatCache);
        ReflectionTestUtils.setField(target, "nOptBakMapper", nOptBakMapper);
        ReflectionTestUtils.setField(target, "txBakMapper", txBakMapper);
        NetworkStat networkStat = mock(NetworkStat.class);
        when(networkStatCache.getNetworkStat()).thenReturn(networkStat);
        when(networkStat.getTxQty()).thenReturn(1000);
        when(blockParameterService.getParameters(any())).thenReturn(nodeOptList);
        when(transactionParameterService.getParameters(any())).thenReturn(nodeOptList);
        when(txBakMapper.deleteByExample(any())).thenReturn(100);
        when(nOptBakMapper.deleteByExample(any())).thenReturn(100);
    }

    @Test(expected = Exception.class)
    public void test() throws Exception {
        CollectionEvent event = CollectionEvent.builder()
                .block(blockList.get(0))
                .transactions(new ArrayList<>(transactionList))
                .epochMessage(EpochMessage.newInstance())
                .build();
        target.onEvent(event,33,false);
        verify(target, times(1)).onEvent(any(),anyLong(),anyBoolean());

        doThrow(new RuntimeException("")).when(blockParameterService).getParameters(any());
        target.onEvent(event,33,false);
    }
}
