package com.platon.browser.collection.queue.handler;

import com.platon.browser.AgentTestBase;
import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.event.BootstrapEvent;
import com.platon.browser.bootstrap.queue.handler.BootstrapEventHandler;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.collection.queue.event.BlockEvent;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.queue.collection.publisher.CollectionEventPublisher;
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
public class BlockEventHandlerTest extends AgentTestBase {
    @Mock
    private CollectionEventPublisher collectionEventPublisher;
    @Spy
    private BlockEventHandler target;

    private ReceiptResult receiptResult;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "collectionEventPublisher", collectionEventPublisher);
        receiptResult = receiptResultList.get(0);
    }

    @Test
    public void test() throws InterruptedException, ExecutionException, BeanCreateOrUpdateException {
        CompletableFuture<PlatonBlock> blockCF=getBlockAsync(7000L);
        CompletableFuture<ReceiptResult> receiptCF=getReceiptAsync(7000L);
        BlockEvent blockEvent = BlockEvent.builder()
                .blockCF(blockCF)
                .receiptCF(receiptCF)
                .epochMessage(EpochMessage.newInstance())
                .build();

        target.onEvent(blockEvent,1,false);

        verify(target, times(1)).onEvent(any(),anyLong(),anyBoolean());
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
