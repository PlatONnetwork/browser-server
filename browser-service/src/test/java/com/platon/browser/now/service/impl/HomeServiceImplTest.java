package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.StakingListNewResp;

public class HomeServiceImplTest extends TestBase {
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HomeService homeService;
	
	@Test
	public void queryNavigation() {
		QueryNavigationRequest req = new QueryNavigationRequest();
		req.setParameter("3");
		QueryNavigationResp resp = homeService.queryNavigation(req);
		assertNotNull(resp);
		
		try {
			req.setParameter("0x");
			resp = homeService.queryNavigation(req);
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
		
		req.setParameter("0x1000000000000000000000000000000000000001");
		resp = homeService.queryNavigation(req);
		assertNotNull(resp);
		
		req.setParameter("0x00dbaac981611fa0cf5b7c1ea97c5a5ae3f69b30966132a2d084c4187b0dcb6d");
		resp = homeService.queryNavigation(req);
		assertNotNull(resp);
		
		req.setParameter("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
		resp = homeService.queryNavigation(req);
		assertNotNull(resp);
		
		req.setParameter("cdm-004");
		resp = homeService.queryNavigation(req);
		assertNotNull(resp);
	}
	
	@Test
	public void blockStatisticNew() {
		BlockStatisticNewResp resp = homeService.blockStatisticNew();
		assertNotNull(resp);
	}
	
	@Test
	public void chainStatisticNew() {
		ChainStatisticNewResp resp = homeService.chainStatisticNew();
		assertNotNull(resp);
	}
	
	@Test
	public void blockListNew() {
		List<BlockListNewResp> list = homeService.blockListNew();
		assertTrue(list.size()>0);
	}
	
	@Test
	public void stakingListNew() {
		StakingListNewResp list = homeService.stakingListNew();
		assertNotNull(list);
	}
	
}
