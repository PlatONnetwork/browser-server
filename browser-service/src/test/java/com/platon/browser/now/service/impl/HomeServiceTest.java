package com.platon.browser.now.service.impl;


import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.res.home.StakingListNewResp;
import com.platon.browser.util.I18nUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HomeServiceTest {
	@Mock
	private BlockESRepository blockESRepository;
	@Mock
	private TransactionESRepository transactionESRepository;
	@Mock
	private StatisticCacheService statisticCacheService;
	@Mock
	private I18nUtil i18n;
	@Mock
	private NodeMapper nodeMapper;
	@Mock
	private AddressMapper addressMapper;
	@Mock
	private BlockChainConfig blockChainConfig;
	@Mock
	private CommonService commonService;
	@Mock
	private CustomNodeMapper customNodeMapper;
    @Spy
    private HomeServiceImpl target;

    @Before
	public void setup() {
        ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
        ReflectionTestUtils.setField(target,"blockESRepository",blockESRepository);
        ReflectionTestUtils.setField(target,"transactionESRepository",transactionESRepository);
        ReflectionTestUtils.setField(target,"nodeMapper",nodeMapper);
        ReflectionTestUtils.setField(target,"addressMapper",addressMapper);
        ReflectionTestUtils.setField(target,"i18n",i18n);
        ReflectionTestUtils.setField(target,"blockChainConfig",blockChainConfig);
        ReflectionTestUtils.setField(target,"commonService",commonService);
        ReflectionTestUtils.setField(target,"customNodeMapper",customNodeMapper);

        when(blockChainConfig.getMinProposalTextSupportRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalTextParticipationRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalUpgradePassRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getParamProposalSupportRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getParamProposalVoteRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalCancelSupportRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getMinProposalCancelParticipationRate()).thenReturn(BigDecimal.ONE);
        when(blockChainConfig.getConsensusPeriodBlockCount()).thenReturn(BigInteger.ONE);
    }

	@Test
	public void stakingListNew() {
		NetworkStat net = new NetworkStat();
		net.setTxQty(10);
		net.setStakingDelegationValue(BigDecimal.TEN);
		net.setStakingValue(BigDecimal.ONE);
		net.setSettleStakingReward(BigDecimal.TEN);
		net.setCurNumber(1000000l);
		when(statisticCacheService.getNetworkStatCache()).thenReturn(net);
		List<Node> nodes = new ArrayList<Node>();
		Node node = new Node();
		node.setIsInit(1);
		node.setStakingHes(BigDecimal.TEN);
		node.setStakingLocked(BigDecimal.TEN);
		node.setStakingReduction(BigDecimal.TEN);
		node.setStatDelegateValue(BigDecimal.TEN);
		nodes.add(node);
		node.setAnnualizedRate(10.0);
		node.setIsInit(0);
		nodes.add(node);
		when(nodeMapper.selectByExample(any())).thenReturn(nodes);
		StakingListNewResp list = target.stakingListNew();
		assertNotNull(list);
	}
}