package com.platon.browser.now.service.impl;


import com.github.pagehelper.Page;
import com.platon.browser.TestMockBase;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.CustomDelegationMapper;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.DelegationAddress;
import com.platon.browser.dto.DelegationStaking;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.DelegationListByAddressResp;
import com.platon.browser.res.staking.DelegationListByStakingResp;
import com.platon.browser.res.staking.StakingStatisticNewResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakingServiceTest extends TestMockBase{
    @Mock
    private CustomStakingMapper customStakingMapper;
    @Mock
    private CustomDelegationMapper customDelegationMapper;
    @Mock
    private CustomNodeMapper customNodeMapper;
    @Mock
    private NodeOptESRepository nodeOptESRepository;
    @Mock
    private PlatOnClient platonClient;
    @Spy
    private StakingServiceImpl target;

    @Before
	public void setup() {
        ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
        ReflectionTestUtils.setField(target,"customStakingMapper",customStakingMapper);
        ReflectionTestUtils.setField(target,"customDelegationMapper",customDelegationMapper);
        ReflectionTestUtils.setField(target,"nodeMapper",nodeMapper);
        ReflectionTestUtils.setField(target,"nodeOptESRepository",nodeOptESRepository);
        ReflectionTestUtils.setField(target,"i18n",i18n);
        ReflectionTestUtils.setField(target,"blockChainConfig",blockChainConfig);
        ReflectionTestUtils.setField(target,"platonClient",platonClient);
        ReflectionTestUtils.setField(target,"customNodeMapper",customNodeMapper);

    }

    @Test
    public void testDetails() throws IOException {
        HistoryStakingListReq req = new HistoryStakingListReq();
        req.setKey("test");

        Page<Node> nodeListPage = new Page<>();
        nodeListPage.addAll(nodeList);
        when(customNodeMapper.selectListByExample(any())).thenReturn(nodeListPage);
        target.historyStakingList(req);
        AliveStakingListReq aliveStakingListReq = new AliveStakingListReq(); 
        aliveStakingListReq.setQueryStatus("all");
        target.aliveStakingList(aliveStakingListReq);


        StakingDetailsReq stakingDetailsReq = new StakingDetailsReq();
        stakingDetailsReq.setNodeId("0xdssfsf");
        
        Node staking = new Node();
        staking.setNodeName("test");
        staking.setNodeIcon("icon");
        staking.setLeaveTime(new Date());
        staking.setStatSlashLowQty(3);
        staking.setStatSlashMultiQty(3);
        staking.setStatDelegateReleased(BigDecimal.ONE);
        staking.setStatus(CustomStaking.StatusEnum.EXITING.getCode());
        staking.setIsConsensus(2);
        staking.setIsSettle(2);
        staking.setDeleAnnualizedRate(9.3);
        staking.setTotalDeleReward(BigDecimal.ONE);
        staking.setPreTotalDeleReward(BigDecimal.ONE);
        staking.setHaveDeleReward(BigDecimal.TEN);
        staking.setRewardPer(333);
        staking.setIsInit(1);
        staking.setStakingLocked(BigDecimal.ONE);
        staking.setStatDelegateValue(BigDecimal.ONE);
        staking.setStakingReduction(BigDecimal.ONE);
        staking.setJoinTime(new Date());
        when(nodeMapper.selectByPrimaryKey(any())).thenReturn(staking);
        target.stakingDetails(stakingDetailsReq);

        staking.setStakingHes(BigDecimal.ONE);
        staking.setStakingLocked(BigDecimal.ONE);
        staking.setWebSite("www.cdm.com");
        staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
        target.stakingDetails(stakingDetailsReq);

        StakingOptRecordListReq stakingOptRecordListReq = new StakingOptRecordListReq();
        stakingOptRecordListReq.setNodeId("test");
        stakingOptRecordListReq.setPageNo(2);
        stakingOptRecordListReq.setPageSize(3);
        stakingOptRecordListReq.setPager(new Page<>());
        ESResult items = new ESResult<>();
        List<NodeOpt> opts = new ArrayList<>();
        NodeOpt nodeOpt = new NodeOpt();
        opts.add(nodeOpt);
        items.setRsData(opts);
        items.setTotal(3L);

        nodeOpt.setTime(new Date());
        nodeOpt.setDesc("test|4|3|4|5");
        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.PROPOSALS.getCode()));
        when(nodeOptESRepository.search(any(),any(),anyInt(),anyInt())).thenReturn(items);
        target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.VOTE.getCode()));
        target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.MULTI_SIGN.getCode()));
        target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
        target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.PARAMETER.getCode()));
        target.stakingOptRecordList(stakingOptRecordListReq);

        assertTrue(true);
    }
    
    @Test
	public void testStakingStatisticNew() {
		when(customStakingMapper.selectCountByActive()).thenReturn(10);
		StakingStatisticNewResp resp = target.stakingStatisticNew();
		assertNotNull(resp);
	}
    
    @Test
	public void testDelegationListByStaking() {
    	DelegationListByStakingReq req = new DelegationListByStakingReq();
    	Page<DelegationStaking> delegationStakingPage = new Page<DelegationStaking>();
    	List<DelegationStaking> delegationStakings = new ArrayList<DelegationStaking>();
    	DelegationStaking delegationStaking = new DelegationStaking();
    	delegationStaking.setDelegateHes(BigDecimal.TEN);
    	delegationStaking.setDelegateLocked(BigDecimal.TEN);
    	delegationStakings.add(delegationStaking);
    	delegationStakingPage.add(delegationStaking);
    	when(customDelegationMapper.selectStakingByNodeId(any())).thenReturn(delegationStakingPage);
    	RespPage<DelegationListByStakingResp> resp = target.delegationListByStaking(req);
    	assertNotNull(resp);
    }
    
    @Test
	public void testDelegationListByAddress() {
    	DelegationListByAddressReq req = new DelegationListByAddressReq();
    	Page<DelegationAddress> delegationAddressPage = new Page<DelegationAddress>();
    	List<DelegationAddress> delegationAddresses = new ArrayList<DelegationAddress>();
    	DelegationAddress delegationAddress = new DelegationAddress();
    	delegationAddress.setDelegateHes(BigDecimal.TEN);
    	delegationAddress.setDelegateLocked(BigDecimal.TEN);
    	delegationAddresses.add(delegationAddress);
    	delegationAddressPage.add(delegationAddress);
    	when(customDelegationMapper.selectAddressByAddr(any())).thenReturn(delegationAddressPage);
    	RespPage<DelegationListByAddressResp> resp = target.delegationListByAddress(req);
    	assertNotNull(resp);
    }
}