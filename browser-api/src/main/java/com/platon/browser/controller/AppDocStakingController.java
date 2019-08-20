package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.dto.RespPage;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.StakingService;
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
import com.platon.browser.util.I18nUtil;

@RestController
public class AppDocStakingController implements AppDocStaking {

//	private final Logger logger = LoggerFactory.getLogger(AppDocStakingController.class);
	
	@Autowired
	private I18nUtil i18n;
	
	@Autowired
	private StakingService stakingService;
	
	@Override
	public BaseResp<StakingStatisticNewResp> stakingStatisticNew() {
		StakingStatisticNewResp stakingStatisticNewResp = stakingService.stakingStatisticNew();
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),stakingStatisticNewResp);
	}

	@Override
	public RespPage<AliveStakingListResp> aliveStakingList(@Valid AliveStakingListReq req) {
		return stakingService.aliveStakingList(req);
	}

	@Override
	public RespPage<HistoryStakingListResp> historyStakingList(@Valid HistoryStakingListReq req) {
		return stakingService.historyStakingList(req);
	}

	@Override
	public BaseResp<StakingChangeNewResp> stakingChangeNew() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<StakingDetailsResp> stakingDetails(@Valid StakingDetailsReq req) {
		return stakingService.stakingDetails(req);
	}

	@Override
	public RespPage<StakingOptRecordListResp> stakingOptRecordList(@Valid StakingOptRecordListReq req) {
		return stakingService.stakingOptRecordList(req);
	}

	@Override
	public RespPage<DelegationListByStakingResp> delegationListByStaking(@Valid DelegationListByStakingReq req) {
		return stakingService.delegationListByStaking(req);
	}

	@Override
	public RespPage<DelegationListByAddressResp> delegationListByAddress(@Valid DelegationListByAddressReq req) {
		return stakingService.delegationListByAddress(req);
	}

}
