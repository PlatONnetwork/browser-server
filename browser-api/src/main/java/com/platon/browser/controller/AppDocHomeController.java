package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.StakingListNewResp;

@RestController
public class AppDocHomeController implements AppDocHome {

	@Override
	public BaseResp<QueryNavigationResp> queryNavigation(@Valid QueryNavigationRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<BlockStatisticNewResp> blockStatisticNew() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<ChainStatisticNewResp> chainStatisticNew() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<BlockListNewResp> blockListNew() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<StakingListNewResp> stakingListNew() {
		// TODO Auto-generated method stub
		return null;
	}

}
