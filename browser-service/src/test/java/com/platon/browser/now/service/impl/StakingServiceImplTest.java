package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.AliveStakingListResp;
import com.platon.browser.res.staking.DelegationListByAddressResp;
import com.platon.browser.res.staking.DelegationListByStakingResp;
import com.platon.browser.res.staking.HistoryStakingListResp;
import com.platon.browser.res.staking.StakingDetailsResp;
import com.platon.browser.res.staking.StakingOptRecordListResp;
import com.platon.browser.res.staking.StakingStatisticNewResp;

public class StakingServiceImplTest extends TestBase{

	@Autowired
    private StakingService stakingService;
	
//	@Test
//	public void stakingStatisticNew() {
//		StakingStatisticNewResp resp = this.stakingService.stakingStatisticNew();
//		assertNotNull(resp);
//	}
	
	@Test
	public void aliveStakingList() {
		AliveStakingListReq req = new AliveStakingListReq();
		req.setQueryStatus("all");
		RespPage<AliveStakingListResp> resp = stakingService.aliveStakingList(req);
		assertNotNull(resp);
		
		req.setQueryStatus("active");
		resp = stakingService.aliveStakingList(req);
		assertNotNull(resp);
		
		req.setQueryStatus("candidate");
		resp = stakingService.aliveStakingList(req);
		assertNotNull(resp);
	}
	
	@Test
	public void historyStakingList() {
		HistoryStakingListReq req = new HistoryStakingListReq();
		RespPage<HistoryStakingListResp> resp = stakingService.historyStakingList(req);
		assertNotNull(resp);
	}
	
	@Test
	public void stakingDetails() {
		StakingDetailsReq req = new StakingDetailsReq();
		req.setNodeId("0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc");
		BaseResp<StakingDetailsResp> resp = stakingService.stakingDetails(req);
		assertNotNull(resp);
	}
	
	@Test
	public void stakingOptRecordList() {
		StakingOptRecordListReq req = new StakingOptRecordListReq();
		req.setNodeId("0xbfc9d6578bab4e510755575e47b7d137fcf0ad0bcf10ed4d023640dfb41b197b9f0d8014e47ecbe4d51f15db514009cbda109ebcf0b7afe06600d6d423bb7fbf");
		RespPage<StakingOptRecordListResp> resp = stakingService.stakingOptRecordList(req);
		assertNotNull(resp);
	}
	
	@Test
	public void delegationListByStaking() {
		DelegationListByStakingReq req = new DelegationListByStakingReq();
		req.setNodeId("0x0aa9805681d8f77c05f317efc141c97d5adb511ffb51f5a251d2d7a4a3a96d9a12adf39f06b702f0ccdff9eddc1790eb272dca31b0c47751d49b5931c58701e7");
		req.setStakingBlockNum("53086");
		RespPage<DelegationListByStakingResp> resp = stakingService.delegationListByStaking(req);
		assertNotNull(resp);
	}
	
	@Test
	public void delegationListByAddress() {
		DelegationListByAddressReq req = new DelegationListByAddressReq();
		req.setAddress("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
		RespPage<DelegationListByAddressResp> resp = stakingService.delegationListByAddress(req);
		assertNotNull(resp);
	}
	
}
