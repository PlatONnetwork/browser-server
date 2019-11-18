package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.service.elasticsearch.EsDelegationService;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.DelegationMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateMigrateTaskTest extends AgentTestBase {
    @Mock
    private DelegationMapper delegationMapper;
    @Mock
    private EsDelegationService esDelegationService;
    @Spy
    private DelegateMigrateTask target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "delegationMapper", delegationMapper);
        ReflectionTestUtils.setField(target, "esDelegationService", esDelegationService);

        when(delegationMapper.selectByExample(any())).thenReturn(new ArrayList<>(delegationList));
    }

    @Test
    public void test() throws IOException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);

        target.cron();
        verify(target, times(1)).cron();

        doThrow(new RuntimeException("")).when(delegationMapper).selectByExample(any());
        target.cron();
    }
}
