package com.platon.browser.task;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.common.utils.AppStatusUtil;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.dao.mapper.AddressMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 17:13:04
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class AddressUpdateTaskTest extends AgentTestBase {
    @Mock
    private StatisticBusinessMapper statisticBusinessMapper;
    @Mock
    private AddressMapper addressMapper;

    @Spy
    private AddressUpdateTask target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "statisticBusinessMapper", statisticBusinessMapper);
        ReflectionTestUtils.setField(target, "addressMapper", addressMapper);

        when(addressMapper.selectByExample(any())).thenReturn(new ArrayList<>(addressList));
        when(statisticBusinessMapper.getAddressStatisticsFromStaking(any())).thenReturn(Collections.emptyList());
        when(statisticBusinessMapper.getAddressStatisticsFromDelegation(any())).thenReturn(Collections.emptyList());
    }

    @Test
    public void test(){
        AppStatusUtil.setStatus(AppStatus.RUNNING);
        target.batchUpdate(4,55);
        //verify(target, times(1)).batchUpdate(any(),any());
    }
}
