package com.platon.browser.task;

import com.platon.browser.AgentTestData;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.handler.PersistenceEventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigInteger;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class BatchSizeAdjustTaskTest extends AgentTestData {
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private PersistenceEventHandler persistenceEventHandler;
    @InjectMocks
    @Spy
    private BatchSizeAdjustTask target;

    @Before
    public void setup() throws Exception {
        when(platOnClient.getLatestBlockNumber()).thenReturn(BigInteger.TEN);
    }

    @Test
    public void test() throws IOException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);

        when(persistenceEventHandler.getMaxBlockNumber()).thenReturn(BigInteger.TEN.longValue());
        target.batchSizeAdjust();
        when(persistenceEventHandler.getMaxBlockNumber()).thenReturn(100L);
        target.batchSizeAdjust();
        verify(target, times(2)).batchSizeAdjust();

        doThrow(new RuntimeException("")).when(platOnClient).getLatestBlockNumber();
        target.batchSizeAdjust();
    }
}
