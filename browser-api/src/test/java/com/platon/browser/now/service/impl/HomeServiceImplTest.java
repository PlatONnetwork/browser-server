package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.StakingListNewResp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class HomeServiceImplTest {
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HomeService homeService;
	
	@Test
	public void queryNavigation() {
		QueryNavigationRequest req = new QueryNavigationRequest();
		req.setParameter("3");
		QueryNavigationResp resp = homeService.queryNavigation(req);
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
