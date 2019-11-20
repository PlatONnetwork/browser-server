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
	
	@Test
	public void stakingStatisticNew() {
		StakingStatisticNewResp resp = this.stakingService.stakingStatisticNew();
		assertNotNull(resp);
	}
	
	@Test
	public void aliveStakingList() {
		AliveStakingListReq req = new AliveStakingListReq();
		req.setKey("cdm-004");
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
		req.setKey("cdm-004");
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
		req.setNodeId("0xef97cb9caf757c70e9aca9062a9f6607ce89c3e7cac90ffee56d3fcffffa55aebd20b48c0db3924438911fd1c88c297d6532b434c56dbb5d9758f0794c6841dc");
		RespPage<StakingOptRecordListResp> resp = stakingService.stakingOptRecordList(req);
		assertNotNull(resp);
	}
	
	@Test
	public void delegationListByStaking() {
		DelegationListByStakingReq req = new DelegationListByStakingReq();
		req.setNodeId("0x00cc251cf6bf3ea53a748971a223f5676225ee4380b65c7889a2b491e1551d45fe9fcc19c6af54dcf0d5323b5aa8ee1d919791695082bae1f86dd282dba41000");
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
