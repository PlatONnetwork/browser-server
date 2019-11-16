package com.platon.browser.bootstrap.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.publisher.BootstrapEventPublisher;
import com.platon.browser.collection.service.block.BlockService;
import com.platon.browser.collection.service.receipt.ReceiptService;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ConsistencyServiceTest extends AgentTestBase {
    @Mock
    private NetworkStatMapper networkStatMapper;
    @Mock
    private BlockESRepository blockESRepository;
    @Mock
    private BlockService blockService;
    @Mock
    private ReceiptService receiptService;
    @Mock
    private BootstrapEventPublisher bootstrapEventPublisher;
    @Mock
    private ShutdownCallback shutdownCallback;
    @Spy
    private ConsistencyService target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "networkStatMapper", networkStatMapper);
        ReflectionTestUtils.setField(target, "blockESRepository", blockESRepository);
        ReflectionTestUtils.setField(target, "blockService", blockService);
        ReflectionTestUtils.setField(target, "receiptService", receiptService);
        ReflectionTestUtils.setField(target, "bootstrapEventPublisher", bootstrapEventPublisher);
        ReflectionTestUtils.setField(target, "shutdownCallback", shutdownCallback);
        when(shutdownCallback.isDone()).thenReturn(true);
    }

    @Test
    public void post() throws IOException {
        NetworkStat networkStat = null;
        when(networkStatMapper.selectByPrimaryKey(anyInt())).thenReturn(networkStat);
        target.post();

        networkStat = CollectionNetworkStat.newInstance();
        networkStat.setCurNumber(10L);
        when(networkStatMapper.selectByPrimaryKey(anyInt())).thenReturn(networkStat);
        when(blockESRepository.exists(anyString())).thenReturn(false);
        target.post();
        when(blockESRepository.exists(anyString())).thenReturn(true);
        target.post();

        when(blockService.getBlockAsync(anyLong())).thenReturn(null);
        when(receiptService.getReceiptAsync(anyLong())).thenReturn(null);
        target.post();

        verify(target, times(4)).post();
    }

}
