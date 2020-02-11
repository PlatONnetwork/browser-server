package com.platon.browser.now.service.impl;


import com.github.pagehelper.Page;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.CustomDelegationMapper;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.util.I18nUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakingServiceTest {
    @Mock
    private StatisticCacheService statisticCacheService;
    @Mock
    private CustomStakingMapper customStakingMapper;
    @Mock
    private CustomDelegationMapper customDelegationMapper;
    @Mock
    private NodeMapper nodeMapper;
    @Mock
    private CustomNodeMapper customNodeMapper;
    @Mock
    private NodeOptESRepository nodeOptESRepository;
    @Mock
    private I18nUtil i18n;
    @Mock
    private BlockChainConfig blockChainConfig;
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

        when(blockChainConfig.getMinProposalTextSupportRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalTextParticipationRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalUpgradePassRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getParamProposalSupportRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getParamProposalVoteRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalCancelSupportRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalCancelParticipationRate()).thenReturn(BigDecimal.ONE);
    }

    @Test
    public void test() throws IOException {
        HistoryStakingListReq req = new HistoryStakingListReq();
        req.setKey("test");
        Page<Node> stakings = new Page();
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
        stakings.add(staking);

        Page<Node> nodeList = new Page<>();
        nodeList.addAll(stakings);
        when(customNodeMapper.selectListByExample(any())).thenReturn(nodeList);
        target.historyStakingList(req);


        StakingDetailsReq stakingDetailsReq = new StakingDetailsReq();
        stakingDetailsReq.setNodeId("0xdssfsf");
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
}