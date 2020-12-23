package com.platon.browser.bootstrap.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.publisher.BootstrapEventPublisher;
import com.platon.browser.collection.service.block.BlockService;
import com.platon.browser.collection.service.receipt.ReceiptService;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.complement.dao.mapper.SyncTokenInfoMapper;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.dto.elasticsearch.TokenTxSummary;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.InnerTxESRepository;
import com.platon.browser.now.service.Erc20TransactionSyncService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@matrixelements.com
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
    @Mock
    private InnerTxESRepository tokenTxESRepository;
    @Mock
    private SyncTokenInfoMapper syncTokenInfoMapper;
    @Mock
    private Erc20TransactionSyncService erc20TransactionSyncService;

    @InjectMocks
    @Spy
    private ConsistencyService target;

    @Before
    public void setup() {
        when(shutdownCallback.isDone()).thenReturn(true);
    }

    @Test
    public void post() throws IOException {

        TokenTxSummary summary = new TokenTxSummary();
        when(tokenTxESRepository.groupContractTxCount()).thenReturn(summary);

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
