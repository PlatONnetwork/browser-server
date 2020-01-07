package com.platon.browser.common.service.elasticsearch;

import com.platon.browser.AgentTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EsImportServiceTest extends AgentTestBase {
    @Mock
    private EsBlockService blockService;
    @Mock
    private EsTransactionService transactionService;
    @Mock
    private EsNodeOptService nodeOptService;
    @Spy
    private EsImportService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "blockService", blockService);
        ReflectionTestUtils.setField(target, "transactionService", transactionService);
        ReflectionTestUtils.setField(target, "nodeOptService", nodeOptService);
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test
    public void batchImport() throws InterruptedException {
        target.batchImport(Collections.emptySet(),Collections.emptySet(),Collections.emptySet(), Collections.emptySet());
        verify(target, times(1)).batchImport(anySet(),anySet(),anySet(),anySet());
    }
}
