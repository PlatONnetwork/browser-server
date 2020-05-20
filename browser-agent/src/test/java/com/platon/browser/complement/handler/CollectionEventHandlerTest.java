package com.platon.browser.complement.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.queue.complement.publisher.ComplementEventPublisher;
import com.platon.browser.complement.bean.TxAnalyseResult;
import com.platon.browser.complement.service.BlockParameterService;
import com.platon.browser.complement.service.StatisticParameterService;
import com.platon.browser.complement.service.TransactionParameterService;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.CustomNOptBakMapper;
import com.platon.browser.dao.mapper.CustomTxBakMapper;
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
    @Mock
    private CustomTxBakMapper customTxBakMapper;
    @Mock
    private CustomNOptBakMapper customNOptBakMapper;

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
        ReflectionTestUtils.setField(target, "nOptBakMapper", nOptBakMapper);
        ReflectionTestUtils.setField(target, "txBakMapper", txBakMapper);
        ReflectionTestUtils.setField(target, "customTxBakMapper", customTxBakMapper);
        ReflectionTestUtils.setField(target, "customNOptBakMapper", customNOptBakMapper);
        NetworkStat networkStat = mock(NetworkStat.class);
        when(networkStatCache.getNetworkStat()).thenReturn(networkStat);
        when(networkStat.getTxQty()).thenReturn(1000);
        when(blockParameterService.getParameters(any())).thenReturn(nodeOptList);
        TxAnalyseResult txAnalyseResult = TxAnalyseResult.builder()
                .nodeOptList(nodeOptList)
                .delegationRewardList(new ArrayList<>())
                .build();
        when(transactionParameterService.getParameters(any())).thenReturn(txAnalyseResult);
        when(txBakMapper.deleteByExample(any())).thenReturn(100);
        when(nOptBakMapper.deleteByExample(any())).thenReturn(100);
    }

    @Test
    public void test() throws Exception {
        CollectionEvent event = new CollectionEvent();
        event.setBlock(blockList.get(0));
        event.setEpochMessage(EpochMessage.newInstance());
        event.setTransactions(new ArrayList <>(transactionList));
        when(customTxBakMapper.batchInsertOrUpdateSelective(any(),any())).thenReturn(100);
        target.onEvent(event,33,false);
        verify(target, times(1)).onEvent(any(),anyLong(),anyBoolean());

        doThrow(new RuntimeException("")).when(blockParameterService).getParameters(any());
        try {
        	target.onEvent(event,33,false);
		} catch (Exception e) {
		}
    }
}
