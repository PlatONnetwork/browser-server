package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.staking.*;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.staking.*;
import com.platon.browser.service.StakingService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 验证人模块Contract。定义使用方法
 * 
 * @file AppDocStakingController.java
 * @description
 * @author zhangrj
 * @data 2019年8月31日
 */
@Slf4j
@RestController
public class StakingController {

	@Resource
	private I18nUtil i18n;
	@Resource
	private StakingService stakingService;

	@SubscribeMapping("topic/staking/statistic/new")
	@PostMapping("staking/statistic")
	public Mono<BaseResp<StakingStatisticNewResp>> stakingStatisticNew() {
		return Mono.create(sink -> {
			StakingStatisticNewResp resp = stakingService.stakingStatisticNew();
			sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),resp));
		});
	}

	@PostMapping("staking/aliveStakingList")
	public Mono<RespPage<AliveStakingListResp>> aliveStakingList(@Valid @RequestBody AliveStakingListReq req) {
		return Mono.just(stakingService.aliveStakingList(req));
	}

	@PostMapping("staking/historyStakingList")
	public Mono<RespPage<HistoryStakingListResp>> historyStakingList(@Valid @RequestBody HistoryStakingListReq req) {
		return Mono.just(stakingService.historyStakingList(req));
	}

	@PostMapping("staking/stakingDetails")
	public Mono<BaseResp<StakingDetailsResp>> stakingDetails(@Valid @RequestBody StakingDetailsReq req) {
		return Mono.just(stakingService.stakingDetails(req));
	}

	@PostMapping("staking/stakingOptRecordList")
	public Mono<RespPage<StakingOptRecordListResp>> stakingOptRecordList(@Valid @RequestBody StakingOptRecordListReq req) {
		return Mono.just(stakingService.stakingOptRecordList(req));
	}

	@PostMapping("staking/delegationListByStaking")
	public Mono<RespPage<DelegationListByStakingResp>> delegationListByStaking(@Valid @RequestBody DelegationListByStakingReq req) {
		return Mono.just(stakingService.delegationListByStaking(req));
	}

	@PostMapping("staking/delegationListByAddress")
	public Mono<RespPage<DelegationListByAddressResp>> delegationListByAddress(@Valid @RequestBody DelegationListByAddressReq req) {
		return Mono.just(stakingService.delegationListByAddress(req));
	}

	@PostMapping("staking/lockedStakingList")
	public Mono<RespPage<LockedStakingListResp>> lockedStakingList(@Valid @RequestBody LockedStakingListReq req) {
		return Mono.just(stakingService.lockedStakingList(req));
	}
}
