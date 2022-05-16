package com.platon.browser.service;


import com.github.pagehelper.Page;
import com.platon.browser.ApiTestMockBase;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.bean.DelegationAddress;
import com.platon.browser.bean.DelegationStaking;
import com.platon.browser.bean.StakingBO;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.custommapper.CustomDelegationMapper;
import com.platon.browser.dao.custommapper.CustomNodeMapper;
import com.platon.browser.dao.custommapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.service.elasticsearch.EsNodeOptRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.request.staking.*;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.staking.DelegationListByAddressResp;
import com.platon.browser.response.staking.DelegationListByStakingResp;
import com.platon.browser.response.staking.StakingStatisticNewResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
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
public class StakeEpochServiceTest extends ApiTestMockBase {

    @Mock
    private CustomStakingMapper customStakingMapper;

    @Mock
    private CustomDelegationMapper customDelegationMapper;

    @Mock
    private CustomNodeMapper customNodeMapper;

    @Mock
    private EsNodeOptRepository ESNodeOptRepository;

    @Mock
    private PlatOnClient platonClient;

    @Spy
    private StakingService target;

    @Mock
    private AddressMapper addressMapper;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(this.target, "statisticCacheService", this.statisticCacheService);
        ReflectionTestUtils.setField(this.target, "customStakingMapper", this.customStakingMapper);
        ReflectionTestUtils.setField(this.target, "customDelegationMapper", this.customDelegationMapper);
        ReflectionTestUtils.setField(this.target, "nodeMapper", this.nodeMapper);
        ReflectionTestUtils.setField(this.target, "ESNodeOptRepository", this.ESNodeOptRepository);
        ReflectionTestUtils.setField(this.target, "i18n", this.i18n);
        ReflectionTestUtils.setField(this.target, "blockChainConfig", this.blockChainConfig);
        ReflectionTestUtils.setField(this.target, "platonClient", this.platonClient);
        ReflectionTestUtils.setField(this.target, "customNodeMapper", this.customNodeMapper);
        ReflectionTestUtils.setField(target, "commonService", commonService);
        ReflectionTestUtils.setField(target, "addressMapper", addressMapper);
    }

    @Test
    public void testDetails() throws IOException {
        HistoryStakingListReq req = new HistoryStakingListReq();
        req.setKey("test");

        Page<Node> nodeListPage = new Page<>();
        nodeListPage.addAll(this.nodeList);
        when(this.customNodeMapper.selectListByExample(any())).thenReturn(nodeListPage);
        this.target.historyStakingList(req);
        AliveStakingListReq aliveStakingListReq = new AliveStakingListReq();
        aliveStakingListReq.setQueryStatus("all");
        this.target.aliveStakingList(aliveStakingListReq);

        LockedStakingListReq lockedStakingListReq = new LockedStakingListReq();
        lockedStakingListReq.setKey("test");
        this.target.lockedStakingList(lockedStakingListReq);


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
        staking.setNextRewardPer(444);
        staking.setIsInit(1);
        staking.setStakingLocked(BigDecimal.ONE);
        staking.setStatDelegateValue(BigDecimal.ONE);
        staking.setStakingReduction(BigDecimal.ONE);
        staking.setJoinTime(new Date());
        staking.setDeleAnnualizedRate(120.00);
        staking.setBigVersion(1792);
        staking.setProgramVersion(65535);
        when(nodeMapper.selectByPrimaryKey(any())).thenReturn(staking);
        Address address = new Address();
        address.setType(1);
        when(addressMapper.selectByPrimaryKey(any())).thenReturn(address);
        NetworkStat networkStatRedis =new NetworkStat();
        networkStatRedis.setCurNumber(1000L);
        networkStatRedis.setNodeId("sfadafw55");
        when(statisticCacheService.getNetworkStatCache()).thenReturn(networkStatRedis);
        this.target.stakingDetails(stakingDetailsReq);

        staking.setStakingHes(BigDecimal.ONE);
        staking.setStakingLocked(BigDecimal.ONE);
        staking.setWebSite("www.cdm.com");
        staking.setStatus(CustomStaking.StatusEnum.CANDIDATE.getCode());
        this.target.stakingDetails(stakingDetailsReq);

        StakingOptRecordListReq stakingOptRecordListReq = new StakingOptRecordListReq();
        stakingOptRecordListReq.setNodeId("test");
        stakingOptRecordListReq.setPageNo(2);
        stakingOptRecordListReq.setPageSize(3);
        stakingOptRecordListReq.setPager(new Page<>());
        ESResult<Object> items = new ESResult<>();
        List<Object> opts = new ArrayList<>();
        NodeOpt nodeOpt = new NodeOpt();
        opts.add(nodeOpt);
        items.setRsData(opts);
        items.setTotal(3L);

        nodeOpt.setTime(new Date());
        nodeOpt.setDesc("test|4|3|4|5");
        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.PROPOSALS.getCode()));
        when(this.ESNodeOptRepository.search(any(), any(), anyInt(), anyInt())).thenReturn(items);
        this.target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.VOTE.getCode()));
        this.target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.MULTI_SIGN.getCode()));
        this.target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
        this.target.stakingOptRecordList(stakingOptRecordListReq);

        nodeOpt.setType(Integer.parseInt(NodeOpt.TypeEnum.PARAMETER.getCode()));
        this.target.stakingOptRecordList(stakingOptRecordListReq);

        assertTrue(true);
    }

    @Test
    public void testStakingStatisticNew() {
        StakingBO bo =new StakingBO();
        bo.setStakingDenominator(BigDecimal.ONE);
        bo.setTotalStakingValue(BigDecimal.ONE);
        when(commonService.getTotalStakingValueAndStakingDenominator(any())).thenReturn(bo);
        when(this.customStakingMapper.selectCountByActive()).thenReturn(10);
        StakingStatisticNewResp resp = this.target.stakingStatisticNew();
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
        when(this.customDelegationMapper.selectStakingByNodeId(any())).thenReturn(delegationStakingPage);
        RespPage<DelegationListByStakingResp> resp = this.target.delegationListByStaking(req);
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
        when(this.customDelegationMapper.selectAddressByAddr(any())).thenReturn(delegationAddressPage);
        RespPage<DelegationListByAddressResp> resp = this.target.delegationListByAddress(req);
        assertNotNull(resp);
    }

}