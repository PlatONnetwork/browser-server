package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.AgentTestData;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.service.elasticsearch.EsDelegationService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.DelegationMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateMigrateTaskTest extends AgentTestData {
    @Mock
    private DelegationMapper delegationMapper;
    @Mock
    private EsDelegationService esDelegationService;
    @InjectMocks
    @Spy
    private DelegateMigrateTask target;

    @Before
    public void setup() throws Exception {
        when(delegationMapper.selectByExample(any())).thenReturn(new ArrayList<>(delegationList));
    }

    @Test
    public void test() throws IOException {
        AppStatusUtil.setStatus(AppStatus.RUNNING);

        target.delegateMigrate();
        verify(target, times(1)).delegateMigrate();

        doThrow(new RuntimeException("")).when(delegationMapper).selectByExample(any());
        target.delegateMigrate();
    }
}
