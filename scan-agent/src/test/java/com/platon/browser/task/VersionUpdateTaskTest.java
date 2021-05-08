package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.AgentTestData;
import com.platon.browser.enums.AppStatus;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.CustomStakingHistoryMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class VersionUpdateTaskTest extends AgentTestData {
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private CustomStakingHistoryMapper customStakingHistoryMapper;
    @InjectMocks
    @Spy
    private StakingMigrateTask target;

    @Before
    public void setup() {
        when(stakingMapper.selectByExample(any())).thenReturn(new ArrayList<>(stakingList));
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.stakingMigrate();
        verify(target, times(1)).stakingMigrate();

        doThrow(new RuntimeException("")).when(stakingMapper).selectByExample(any());
        target.stakingMigrate();
    }
}
