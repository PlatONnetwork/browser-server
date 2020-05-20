package com.platon.browser.bootstrap.queue.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.event.BootstrapEvent;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.service.elasticsearch.EsImportService;
import com.platon.browser.common.service.redis.RedisImportService;
import com.platon.browser.dao.entity.TxBak;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.TxBakMapper;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class BootstrapEventHandlerTest extends AgentTestBase {
    @Mock
    private EsImportService esImportService;
    @Mock
    private RedisImportService redisImportService;
    @Mock
    private TxBakMapper txBakMapper;
    @Mock
    private NOptBakMapper nOptBakMapper;
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private AddressCache addressCache;
    @Spy
    private BootstrapEventHandler target;
    private ReceiptResult receiptResult;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "esImportService", esImportService);
        ReflectionTestUtils.setField(target, "redisImportService", redisImportService);
        ReflectionTestUtils.setField(target, "txBakMapper", txBakMapper);
        ReflectionTestUtils.setField(target, "nOptBakMapper", nOptBakMapper);
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(target, "addressCache", addressCache);
        receiptResult = receiptResultList.get(0);
    }

    @Test
    public void test() throws InterruptedException, ExecutionException, BeanCreateOrUpdateException, IOException {
        CompletableFuture<PlatonBlock> blockCF=getBlockAsync(7000L);
        CompletableFuture<ReceiptResult> receiptCF=getReceiptAsync(7000L);
        BootstrapEvent bootstrapEvent = new BootstrapEvent();
        bootstrapEvent.setBlockCF(blockCF);
        bootstrapEvent.setReceiptCF(receiptCF);
        ShutdownCallback sc = new ShutdownCallback();
        sc.setEndBlockNum(7000L);
        bootstrapEvent.setCallback(sc);

        List<TxBak> txBaks = new ArrayList<>();
        TxBak bak = new TxBak();
        bak.setHash(receiptCF.get().getResult().get(0).getTransactionHash());
        bak.setNum(receiptCF.get().getResult().get(0).getBlockNumber());
        bak.setId(100L);
        bak.setInfo("so so");
        txBaks.add(bak);
        when(txBakMapper.selectByExample(any())).thenReturn(txBaks);
        target.onEvent(bootstrapEvent,1,false);

    }

    /**
     * 异步获取区块
     */
    public CompletableFuture<PlatonBlock> getBlockAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(()->{
            PlatonBlock pb = new PlatonBlock();
            PlatonBlock.Block block = rawBlockMap.get(receiptResult.getResult().get(0).getBlockNumber());
            pb.setResult(block);
            return pb;
        });
    }

    /**
     * 异步获取区块
     */
    public CompletableFuture<ReceiptResult> getReceiptAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(()->receiptResult);
    }
}
