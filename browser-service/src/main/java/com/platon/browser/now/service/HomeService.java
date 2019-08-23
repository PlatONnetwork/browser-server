package com.platon.browser.now.service;

import java.util.List;

import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.StakingListNewResp;

public interface HomeService {
    /**
     * 搜索
     */
	public QueryNavigationResp queryNavigation(QueryNavigationRequest req);
	
	/**
	 * 
	 * @return
	 */
	public BlockStatisticNewResp blockStatisticNew();
	
	
	public ChainStatisticNewResp chainStatisticNew();
	
	public List<BlockListNewResp> blockListNew();
	
	public StakingListNewResp stakingListNew();
}
