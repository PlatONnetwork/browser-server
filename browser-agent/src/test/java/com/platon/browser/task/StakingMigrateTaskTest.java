package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.dao.mapper.CustomStakingHistoryMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class StakingMigrateTaskTest extends AgentTestBase {
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private CustomStakingHistoryMapper customStakingHistoryMapper;

    @Spy
    private StakingMigrateTask target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "stakingMapper", stakingMapper);
        ReflectionTestUtils.setField(target, "customStakingHistoryMapper", customStakingHistoryMapper);
        when(stakingMapper.selectByExample(any())).thenReturn(new ArrayList<>(stakingList));
    }

    @Test
    public void test() {
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.cron();
        verify(target, times(1)).cron();

        doThrow(new RuntimeException("")).when(stakingMapper).selectByExample(any());
        target.cron();
    }
}
