package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class RingBufferMonitorTaskTest extends AgentTestBase {
    @InjectMocks
    @Spy
    private RingBufferMonitorTask target;

    @Before
    public void setup() {
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.cron();
        verify(target, times(1)).cron();
    }
}
