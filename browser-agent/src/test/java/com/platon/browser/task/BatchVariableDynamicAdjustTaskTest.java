package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.persistence.queue.handler.PersistenceEventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigInteger;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class BatchVariableDynamicAdjustTaskTest extends AgentTestBase {
    @Mock
    private PlatOnClient platOnClient;
    @Mock
    private PersistenceEventHandler persistenceEventHandler;
    @Spy
    private BatchVariableDynamicAdjustTask target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "platOnClient", platOnClient);
        ReflectionTestUtils.setField(target, "persistenceEventHandler", persistenceEventHandler);

        when(platOnClient.getLatestBlockNumber()).thenReturn(BigInteger.TEN);
    }

    @Test
    public void test() throws IOException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);

        when(persistenceEventHandler.getMaxBlockNumber()).thenReturn(BigInteger.TEN.longValue());
        target.cron();
        when(persistenceEventHandler.getMaxBlockNumber()).thenReturn(100L);
        target.cron();
        verify(target, times(2)).cron();

        doThrow(new RuntimeException("")).when(platOnClient).getLatestBlockNumber();
        target.cron();
    }
}
