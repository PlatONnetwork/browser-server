package com.platon.browser.adjustment.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.adjustment.dao.AdjustmentMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomStaking;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 *
 **/
@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class AdjustServiceTest extends AgentTestBase {
    @Mock
    private DelegationMapper delegationMapper;
    @Mock
    private StakingMapper stakingMapper;
    @Mock
    private NodeMapper nodeMapper;
    @Mock
    private AdjustmentMapper adjustmentMapper;

    private String adjustLogFile = System.getProperty("user.dir")+ File.separator+"adjust.log";

    @Spy
    @InjectMocks
    private AdjustService target;

    @Test
    public void test() throws Exception {
        ReflectionTestUtils.setField(target,"adjustLogFile", adjustLogFile);
        ReflectionTestUtils.invokeMethod(target,"init");
        //target.adjust(adjustParamList);

        CustomStaking staking = stakingList.get(0);
        CustomDelegation delegation = delegationList.get(0);

        when(nodeMapper.selectByPrimaryKey(any())).thenReturn(nodeList.get(0));
        when(stakingMapper.selectByPrimaryKey(any())).thenReturn(staking);
        when(delegationMapper.selectByPrimaryKey(any())).thenReturn(delegation);
        target.adjust(adjustParamList);

        staking.setStakingHes(new BigDecimal("20000000000"));
        staking.setStakingLocked(new BigDecimal(0));
        target.adjust(adjustParamList);

    }
}
