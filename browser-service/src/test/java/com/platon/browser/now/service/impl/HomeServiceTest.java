package com.platon.browser.now.service.impl;


import com.platon.browser.TestMockBase;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.StakingListNewResp;
import com.platon.browser.util.I18nUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HomeServiceTest extends TestMockBase {
	@Mock
	private I18nUtil i18n;
	@Mock
	private BlockChainConfig blockChainConfig;
	@Mock
	private CommonService commonService;
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
		StakingListNewResp list = target.stakingListNew();
		assertNotNull(list);
	}
	
	@Test
	public void testQueryNavigation() throws IOException {
		QueryNavigationRequest req = new QueryNavigationRequest();
		req.setParameter("3");
		QueryNavigationResp list = target.queryNavigation(req);
		try {
			req.setParameter("a");
			list = target.queryNavigation(req);
		} catch (Exception e) {
			
		}
		
		req.setParameter("0x53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3");
		list = target.queryNavigation(req);
		
		req.setParameter("53242dec8799f3f4f8882b109e1a3ebb4aa8c2082d000937d5876365414150c5337aa3d3d41ead1ac873f4e0b19cb9238d2995598207e8d571f0bd5dd843cdf3");
		list = target.queryNavigation(req);
		
		req.setParameter("lax1vr8v48qjjrh9dwvdfctqauz98a7yp5se77fm2e");
		list = target.queryNavigation(req);
		
		req.setParameter("0x9bf480e19c921c93cfc30e2e1d5d67b02b65b89f1f68a675e782ec46478fe228");
		list = target.queryNavigation(req);
		
		when(transactionESRepository.get(any(),any())).thenReturn(null);
		req.setParameter("0x9bf480e19c921c93cfc30e2e1d5d67b02b65b89f1f68a675e782ec46478fe228");
		list = target.queryNavigation(req);
		assertNotNull(list);
	}
	
	@Test
	public void testChainStatisticNew() throws IOException {
		ChainStatisticNewResp chainStatisticNewResp = target.chainStatisticNew();
		assertNotNull(chainStatisticNewResp);
	}
	
	@Test
	public void testBlockStatisticNew() throws IOException {
		BlockStatisticNewResp blockStatisticNewResp = target.blockStatisticNew();
		assertNotNull(blockStatisticNewResp);
	}
}