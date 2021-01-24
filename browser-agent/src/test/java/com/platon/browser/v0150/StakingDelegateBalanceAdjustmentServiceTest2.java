//package com.platon.browser.v015;
//
//import com.alaya.protocol.Web3j;
//import com.alaya.protocol.http.HttpService;
//import com.platon.browser.AgentTestBase;
//import com.platon.browser.bean.CustomStaking;
//import com.platon.browser.client.SpecialApi;
//import com.platon.browser.dao.entity.Delegation;
//import com.platon.browser.dao.entity.Node;
//import com.platon.browser.dao.entity.Staking;
//import com.platon.browser.dao.mapper.DelegationMapper;
//import com.platon.browser.dao.mapper.NodeMapper;
//import com.platon.browser.dao.mapper.StakingMapper;
//import com.platon.browser.exception.BlankResponseException;
//import com.platon.browser.exception.BlockNumberException;
//import com.platon.browser.exception.ContractInvokeException;
//import com.platon.browser.v015.bean.AdjustParam;
//import com.platon.browser.v015.dao.StakingDelegateBalanceAdjustmentMapper;
//import com.platon.browser.v015.service.StakingDelegateBalanceAdjustmentService;
//import lombok.extern.slf4j.Slf4j;
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
//import java.math.BigInteger;
//import java.util.ArrayList;
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
//public class StakingDelegateBalanceAdjustmentServiceTest2 extends AgentTestBase {
//    @Mock
//    private DelegationMapper delegationMapper;
//    @Mock
//    private StakingMapper stakingMapper;
//    @Mock
//    private NodeMapper nodeMapper;
//    @Mock
//    private StakingDelegateBalanceAdjustmentMapper stakingDelegateBalanceAdjustmentMapper;
//    @Mock
//    private V015Config v015Config;
//
//    @Spy
//    @InjectMocks
//    private StakingDelegateBalanceAdjustmentService target;
//
//    private Node node;
//    private Staking staking;
//    private Delegation delegate.estpl.yml;
//    private List<AdjustParam> delegateAdjustParamList = new ArrayList<>();
//    private List<AdjustParam> stakingAdjustParamList = new ArrayList<>();
//
//    private SpecialApi specialApi = new SpecialApi();
//    private Web3j web3j = Web3j.build(new HttpService("http://192.168.21.49:6789"));
//    private Long chainId = 201018L;
//
//    @Before
//    public void setup() throws ContractInvokeException, BlankResponseException {
//        when(v015Config.getAdjustLogFilePath()).thenReturn(System.getProperty("user.dir")+ File.separator+"adjust.log");
//        ReflectionTestUtils.setField(target,"chainConfig", blockChainConfig);
//        ReflectionTestUtils.invokeMethod(target,"init");
//        node = nodeList.get(0);
//        staking = stakingList.get(0);
//        staking.setStakingHes(BigDecimal.ONE);
//        staking.setStakingLocked(BigDecimal.ONE);
//        staking.setStakingReduction(BigDecimal.valueOf(2));
//        delegate.estpl.yml = delegationList.get(0);
//        delegate.estpl.yml.setDelegateHes(BigDecimal.ONE);
//        delegate.estpl.yml.setDelegateLocked(BigDecimal.ONE);
//        delegate.estpl.yml.setDelegateReleased(BigDecimal.valueOf(2));
//
//        adjustParamList = specialApi.getStakingDelegateAdjustDataList(web3j, BigInteger.valueOf(9641));
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
//        when(delegationMapper.selectByPrimaryKey(any())).thenReturn(delegate.estpl.yml);
//    }
//
//    private void adjustDelegate() throws BlockNumberException {
//        String adjustMsg = target.adjust(adjustParamList);
//    }
//
//    private void adjustStaking() throws BlockNumberException {
//        String adjustMsg = target.adjust(adjustParamList);
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
