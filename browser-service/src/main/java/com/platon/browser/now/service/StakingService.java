package com.platon.browser.now.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.resp.staking.*;

import javax.validation.Valid;

public interface StakingService {
	
	public StakingStatisticNewResp stakingStatisticNew();
	
	public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req);
	
	public RespPage<HistoryStakingListResp> historyStakingList(HistoryStakingListReq req);

	BaseResp<StakingChangeNewResp> stakingChangeNew();

	BaseResp<StakingDetailsResp> stakingDetails(@Valid StakingDetailsReq req);

	public RespPage<StakingOptRecordListResp> stakingOptRecordList(@Valid StakingOptRecordListReq req);

	public RespPage<DelegationListByStakingResp> delegationListByStaking(@Valid DelegationListByStakingReq req);

	public RespPage<DelegationListByAddressResp> delegationListByAddress(@Valid DelegationListByAddressReq req);
	
}
