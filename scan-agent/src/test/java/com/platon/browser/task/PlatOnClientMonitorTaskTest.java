package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.AgentTestData;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class PlatOnClientMonitorTaskTest extends AgentTestData {
    @Mock
    private PlatOnClient platOnClient;
    @InjectMocks
    @Spy
    private PlatOnClientMonitorTask target;

    @Before
    public void setup() {

    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.platOnClientMonitor();
        verify(target, times(1)).platOnClientMonitor();

        doThrow(new RuntimeException("")).when(platOnClient).updateCurrentWeb3jWrapper();
        target.platOnClientMonitor();
    }
}
