package com.platon.browser.service;


import com.platon.browser.ApiTestMockBase;
import com.platon.browser.request.home.QueryNavigationRequest;
import com.platon.browser.response.home.BlockStatisticNewResp;
import com.platon.browser.response.home.ChainStatisticNewResp;
import com.platon.browser.response.home.QueryNavigationResp;
import com.platon.browser.response.home.StakingListNewResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HomeServiceTest extends ApiTestMockBase {
    @Spy
    private HomeService target;
    @Spy
    private CommonService service;

    @Before
	public void setup() {
        ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
        ReflectionTestUtils.setField(target,"blockESRepository", ESBlockRepository);
        ReflectionTestUtils.setField(target,"transactionESRepository", ESTransactionRepository);
        ReflectionTestUtils.setField(target,"nodeMapper",nodeMapper);
        ReflectionTestUtils.setField(target,"addressMapper",addressMapper);
        ReflectionTestUtils.setField(target,"blockChainConfig",blockChainConfig);
        ReflectionTestUtils.setField(target,"commonService",commonService);
        ReflectionTestUtils.setField(target,"customNodeMapper",customNodeMapper);
        
        ReflectionTestUtils.setField(service,"customNodeMapper",customNodeMapper);

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
		
		when(ESTransactionRepository.get(any(),any())).thenReturn(null);
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
	
	@Test
	public void testCommonService() throws IOException {
		service.getNodeName("0x", "test");
		when(customNodeMapper.findNameById("0x")).thenReturn("test");
		service.getNodeName("0x", "");
	}
}