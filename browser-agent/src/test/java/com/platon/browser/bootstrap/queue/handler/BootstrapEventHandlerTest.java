package com.platon.browser.bootstrap.queue.handler;

import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.event.BootstrapEvent;
import com.platon.browser.client.result.Receipt;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.common.service.elasticsearch.EsImportService;
import com.platon.browser.common.service.redis.RedisImportService;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class BootstrapEventHandlerTest {
    @Mock
    private EsImportService esImportService;
    @Mock
    private RedisImportService redisImportService;
    @Mock
    private TxBakMapper txBakMapper;
    @Mock
    private NOptBakMapper nOptBakMapper;
    @Spy
    private BootstrapEventHandler target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "esImportService", esImportService);
        ReflectionTestUtils.setField(target, "redisImportService", redisImportService);
        ReflectionTestUtils.setField(target, "txBakMapper", txBakMapper);
        ReflectionTestUtils.setField(target, "nOptBakMapper", nOptBakMapper);
    }

    @Test
    public void test() throws InterruptedException, ExecutionException, BeanCreateOrUpdateException {
        BootstrapEvent bootstrapEvent = BootstrapEvent.builder()
                .blockCF(getBlockAsync(7000L))
                .receiptCF(getReceiptAsync(7000L))
                .callback(ShutdownCallback.builder().endBlockNum(7000L).build())
                .build();
        target.onEvent(bootstrapEvent,1,false);
        verify(target, times(1)).onEvent(any(),anyLong(),anyBoolean());
    }

    /**
     * 异步获取区块
     */
    public CompletableFuture<PlatonBlock> getBlockAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(()->{
            PlatonBlock pb = new PlatonBlock();
            PlatonBlock.Block block = new PlatonBlock.Block();
            block.setHash("0x");
            block.setNumber(blockNumber.toString());
            pb.setResult(block);
            return pb;
        });
    }

    /**
     * 异步获取区块
     */
    public CompletableFuture<ReceiptResult> getReceiptAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(()->{
            ReceiptResult receiptResult = new ReceiptResult();
            List<Receipt> receipts = new ArrayList<>();
            Receipt receipt = new Receipt();
            receipts.add(receipt);
            receiptResult.setResult(receipts);
            return receiptResult;
        });
    }
}
