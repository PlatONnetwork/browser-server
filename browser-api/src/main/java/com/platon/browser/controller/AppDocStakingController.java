package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.resp.staking.AliveStakingListResp;
import com.platon.browser.resp.staking.DelegationListByAddressResp;
import com.platon.browser.resp.staking.DelegationListByStakingResp;
import com.platon.browser.resp.staking.HistoryStakingListResp;
import com.platon.browser.resp.staking.StakingChangeNewResp;
import com.platon.browser.resp.staking.StakingDetailsResp;
import com.platon.browser.resp.staking.StakingOptRecordListResp;
import com.platon.browser.resp.staking.StakingStatisticNewResp;

@RestController
public class AppDocStakingController implements AppDocStaking {

	@Override
	public BaseResp<StakingStatisticNewResp> stakingStatisticNew() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<AliveStakingListResp> aliveStakingList(@Valid AliveStakingListReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<HistoryStakingListResp> historyStakingList(@Valid HistoryStakingListReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<StakingChangeNewResp> stakingChangeNew() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<StakingDetailsResp> stakingDetails(@Valid StakingDetailsReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<StakingOptRecordListResp> stakingOptRecordList(@Valid StakingOptRecordListReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<DelegationListByStakingResp> delegationListByStaking(@Valid DelegationListByStakingReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<DelegationListByAddressResp> delegationListByAddress(@Valid DelegationListByAddressReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
