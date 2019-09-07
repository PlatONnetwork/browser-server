package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.exception.BlockCollectingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 09:32
 * @Description: 交易服务单元测试
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(TransactionServiceTest.class);
    private ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    @Mock
    private PlatonClient client;
    @Mock
    private TransactionService transactionService;

    @Before
    public void setUp() throws IOException, BlockCollectingException {
        ReflectionTestUtils.setField(transactionService, "executor", THREAD_POOL);
        ReflectionTestUtils.setField(transactionService, "client", client);
        when(transactionService.analyze(Mockito.anyList())).thenCallRealMethod();

    }

    @Test
    public void testCollect() throws BlockCollectingException {
        Set<BigInteger> blockNumbers = new HashSet<>();
        for (int i=0;i<20;i++) blockNumbers.add(BigInteger.valueOf(i));
        List<CustomBlock> blocks = transactionService.analyze(this.blocks);
        assertEquals(blockNumbers.size(),blocks.size());
    }

}
