package com.platon.browser.service.redis;

import com.platon.browser.AgentTestBase;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

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
    @InjectMocks
    @Spy
    private RedisImportService target;

    @Before
    public void setup(){
    }


    /**
     * 根据区块号获取激励池余额
     */
    @Test
    public void batchImport() throws Exception {
        target.batchImport(Collections.emptySet(),Collections.emptySet(),Collections.emptySet());
        verify(target, times(1)).batchImport(anySet(),anySet(),anySet());
    }
}
