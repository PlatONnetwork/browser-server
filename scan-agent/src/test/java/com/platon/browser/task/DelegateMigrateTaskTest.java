package com.platon.browser.task;

import com.platon.browser.AgentTestData;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.service.elasticsearch.EsDelegationService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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

    @Before
    public void setup() throws Exception {
        when(delegationMapper.selectByExample(any())).thenReturn(new ArrayList<>(delegationList));
    }

}
