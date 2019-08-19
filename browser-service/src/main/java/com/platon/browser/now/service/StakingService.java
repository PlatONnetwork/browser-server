package com.platon.browser.now.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.resp.staking.AliveStakingListResp;
import com.platon.browser.resp.staking.HistoryStakingListResp;
import com.platon.browser.resp.staking.StakingStatisticNewResp;

public interface StakingService {
	
	public StakingStatisticNewResp stakingStatisticNew();
	
	public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req);
	
	public RespPage<HistoryStakingListResp> historyStakingList(HistoryStakingListReq req);
	
	
	
}
