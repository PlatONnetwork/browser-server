package com.platon.browser.service.redis;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.service.redis.RedisImportService;
import com.platon.browser.service.redis.RedisBlockService;
import com.platon.browser.service.redis.RedisStatisticService;
import com.platon.browser.service.redis.RedisTransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisImportServiceTest extends AgentTestBase {
    @Mock
    private RedisBlockService blockService;
    @Mock
    private RedisTransactionService transactionService;
    @Mock
    private RedisStatisticService statisticService;
    @Spy
    private RedisImportService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "blockService", blockService);
        ReflectionTestUtils.setField(target, "transactionService", transactionService);
        ReflectionTestUtils.setField(target, "statisticService", statisticService);
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test
    public void batchImport() throws InterruptedException {
        target.batchImport(Collections.emptySet(),Collections.emptySet(),Collections.emptySet());
        verify(target, times(1)).batchImport(anySet(),anySet(),anySet());
    }
}
