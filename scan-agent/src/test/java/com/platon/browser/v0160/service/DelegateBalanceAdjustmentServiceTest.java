package com.platon.browser.v0160.service;

import com.platon.browser.cache.AddressCache;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateBalanceAdjustmentServiceTest {

    @Mock
    private NodeMapper nodeMapper;

    @Mock
    private StakingMapper stakingMapper;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private CustomAddressMapper customAddressMapper;

    @Mock
    private AddressCache addressCache;

    @Spy
    private DelegateBalanceAdjustmentService delegateBalanceAdjustmentService;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(delegateBalanceAdjustmentService, "nodeMapper", nodeMapper);
        Node nodeInfo = new Node();
        nodeInfo.setNodeId("aaa");
        nodeInfo.setHaveDeleReward(new BigDecimal(100));
        when(nodeMapper.selectByPrimaryKey(any())).thenReturn(nodeInfo);
        ReflectionTestUtils.setField(delegateBalanceAdjustmentService, "stakingMapper", stakingMapper);
        Staking staking = new Staking();
        staking.setNodeId("aaa");
        staking.setStakingBlockNum(100L);
        staking.setHaveDeleReward(new BigDecimal(100));
        when(stakingMapper.selectByPrimaryKey(any())).thenReturn(staking);
        when(stakingMapper.updateByPrimaryKeySelective(any())).thenReturn(1);
        ReflectionTestUtils.setField(delegateBalanceAdjustmentService, "addressMapper", addressMapper);
        Address addressInfo = new Address();
        addressInfo.setAddress("bbb");
        addressInfo.setHaveReward(new BigDecimal(100));
        when(addressMapper.selectByPrimaryKey(any())).thenReturn(addressInfo);
        ReflectionTestUtils.setField(delegateBalanceAdjustmentService, "customAddressMapper", customAddressMapper);
        when(customAddressMapper.batchUpdateByAddress(any())).thenReturn(1);
        ReflectionTestUtils.setField(delegateBalanceAdjustmentService, "addressCache", addressCache);
        when(addressCache.getAddress(any())).thenReturn(null);
    }

    @Test
    public void adjust() throws Exception {
        delegateBalanceAdjustmentService.adjust();
    }

}
