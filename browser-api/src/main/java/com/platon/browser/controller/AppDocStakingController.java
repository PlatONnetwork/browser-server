package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.req.staking.*;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.resp.staking.*;
import com.platon.browser.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *  验证人模块Contract。定义使用方法
 *  @file AppDocStakingController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class AppDocStakingController implements AppDocStaking {

	@Autowired
	private I18nUtil i18n;
	
	@Autowired
	private StakingService stakingService;
	
	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	
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
	public RespPage<AliveStakingListResp> stakingChangeNew(String message, StompHeaderAccessor stompHeaderAccessor) {
		AliveStakingListReq req = new AliveStakingListReq();
		RespPage<AliveStakingListResp> aliveStakingListResp = stakingService.aliveStakingList(req);
		simpMessageSendingOperations.convertAndSendToUser(stompHeaderAccessor.getUser().getName(), "11",  JSON.toJSONString(aliveStakingListResp));
		return stakingService.aliveStakingList(req);
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
