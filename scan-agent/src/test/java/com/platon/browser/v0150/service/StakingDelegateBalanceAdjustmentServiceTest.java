//package com.platon.browser.v0150.service;
//
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.cache.AddressCache;
//import com.platon.browser.exception.BlockNumberException;
//import com.platon.browser.v0150.V0150Config;
//import com.platon.browser.v0150.bean.AdjustParam;
//import com.platon.browser.dao.entity.Delegation;
//import com.platon.browser.dao.entity.Node;
//import com.platon.browser.dao.entity.Staking;
//import com.platon.browser.dao.mapper.DelegationMapper;
//import com.platon.browser.dao.mapper.NodeMapper;
//import com.platon.browser.dao.mapper.StakingMapper;
//import com.platon.browser.bean.CustomStaking;
//import com.platon.browser.v0150.dao.StakingDelegateBalanceAdjustmentMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
///**
// *
// **/
//@Slf4j
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class StakingDelegateBalanceAdjustmentServiceTest extends AgentTestBase {
//    @Mock
//    private DelegationMapper delegationMapper;
//    @Mock
//    private StakingMapper stakingMapper;
//    @Mock
//    private NodeMapper nodeMapper;
//    @Mock
//    private StakingDelegateBalanceAdjustmentMapper adjustmentMapper;
//    @Mock
//    private V0150Config v0150Config;
//    @Mock
//    private AddressCache addressCache;
//
//    @Spy
//    @InjectMocks
//    private StakingDelegateBalanceAdjustmentService target;
//
//    private Node node;
//    private Staking staking;
//    private Delegation delegation;
//    private List<AdjustParam> delegateAdjustParamList = new ArrayList<>();
//    private List<AdjustParam> stakingAdjustParamList = new ArrayList<>();
//
//    @Before
//    public void setup(){
//        when(v0150Config.getAdjustLogFilePath()).thenReturn(System.getProperty("user.dir")+ File.separator+"adjust.log");
//        ReflectionTestUtils.setField(target,"chainConfig", blockChainConfig);
//        ReflectionTestUtils.invokeMethod(target,"init");
//        node = nodeList.get(0);
//        node.setLeaveTime(new Date());
//        staking = stakingList.get(0);
//        staking.setStakingHes(BigDecimal.ONE);
//        staking.setStakingLocked(BigDecimal.ONE);
//        staking.setStakingReduction(BigDecimal.valueOf(2));
//        staking.setLeaveTime(new Date());
//        delegation = delegationList.get(0);
//        delegation.setDelegateHes(BigDecimal.ONE);
//        delegation.setDelegateLocked(BigDecimal.ONE);
//        delegation.setDelegateReleased(BigDecimal.valueOf(2));
//
//
//        adjustParamList.forEach(param->{
//            if("staking".equals(param.getOptType())){
//                stakingAdjustParamList.add(param);
//            }
//            if("delegate".equals(param.getOptType())){
//                delegateAdjustParamList.add(param);
//            }
//        });
//
//        when(nodeMapper.selectByPrimaryKey(any())).thenReturn(node);
//        when(stakingMapper.selectByPrimaryKey(any())).thenReturn(staking);
//        when(delegationMapper.selectByPrimaryKey(any())).thenReturn(delegation);
//    }
//
//    private void adjustDelegate() throws BlockNumberException {
//        String adjustMsg = target.adjust(adjustParamList);
//        Assert.assertFalse(adjustMsg.contains("错误"));
//
//        //调整委托
//        delegation.setDelegateHes(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(delegateAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：委托记录[犹豫期金额"));
//
//        delegation.setDelegateHes(BigDecimal.ONE);
//        delegation.setDelegateLocked(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(delegateAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：委托记录[锁定期金额"));
//
//        delegation.setDelegateHes(BigDecimal.valueOf(0.5));
//        delegation.setDelegateLocked(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(delegateAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：委托记录[犹豫期金额"));
//        Assert.assertTrue(adjustMsg.contains("【错误】：委托记录[锁定期金额"));
//
//        delegation.setDelegateHes(BigDecimal.TEN);
//        delegation.setDelegateLocked(BigDecimal.TEN);
//        adjustMsg = target.adjust(delegateAdjustParamList);
//        Assert.assertFalse(adjustMsg.contains("错误"));
//
//        //调账质押
//        staking.setStakingHes(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(stakingAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：质押记录[犹豫期金额"));
//
//        staking.setStakingHes(BigDecimal.ONE);
//        staking.setStakingLocked(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(stakingAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：质押记录[锁定期金额"));
//
//        staking.setStakingHes(BigDecimal.valueOf(0.5));
//        staking.setStakingLocked(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(stakingAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：质押记录[犹豫期金额"));
//        Assert.assertTrue(adjustMsg.contains("【错误】：质押记录[锁定期金额"));
//
//        staking.setStakingHes(BigDecimal.TEN);
//        staking.setStakingLocked(BigDecimal.TEN);
//        adjustMsg = target.adjust(stakingAdjustParamList);
//        Assert.assertFalse(adjustMsg.contains("错误"));
//    }
//
//    private void adjustStaking() throws BlockNumberException {
//        String adjustMsg = target.adjust(adjustParamList);
//        Assert.assertFalse(adjustMsg.contains("错误"));
//
//        //调整委托
//        delegation.setDelegateReleased(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(delegateAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：委托记录[待提取金额"));
//
//        delegation.setDelegateReleased(BigDecimal.TEN);
//        adjustMsg = target.adjust(delegateAdjustParamList);
//        Assert.assertFalse(adjustMsg.contains("错误"));
//
//        //调账质押
//        staking.setStakingReduction(BigDecimal.valueOf(0.5));
//        adjustMsg = target.adjust(stakingAdjustParamList);
//        Assert.assertTrue(adjustMsg.contains("【错误】：质押记录[退回中金额"));
//
//        staking.setStakingReduction(BigDecimal.TEN);
//        adjustMsg = target.adjust(stakingAdjustParamList);
//        Assert.assertFalse(adjustMsg.contains("错误"));
//    }
//
//    @Test
//    public void test() throws Exception {
//        // **********************节点是候选中**********************
//        staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
//        adjustDelegate();
//        // **********************节点是已锁定**********************
//        staking.setStatus(CustomStaking.StatusEnum.LOCKED.getCode());
//        adjustDelegate();
//
//        // **********************节点是退出中**********************
//        staking.setStatus(CustomStaking.StatusEnum.EXITING.getCode());
//        adjustStaking();
//        // **********************节点是已退出**********************
//        staking.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
//        adjustStaking();
//    }
//}
