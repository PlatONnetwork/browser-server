package com.platon.browser.controller;

import com.platon.browser.config.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.reqest.staking.*;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.staking.*;
import com.platon.browser.service.StakingService;
import com.platon.browser.util.I18nUtil;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

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
@RestController
public class StakingController {

	@Resource
	private I18nUtil i18n;
	@Resource
	private StakingService stakingService;

	@SubscribeMapping(value = "topic/staking/statistic/new")
	@PostMapping(value = "staking/statistic")
	public WebAsyncTask<BaseResp<StakingStatisticNewResp>> stakingStatisticNew() {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<BaseResp<StakingStatisticNewResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> {
					StakingStatisticNewResp stakingStatisticNewResp = stakingService.stakingStatisticNew();
					return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS),
							stakingStatisticNewResp);
				});
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/aliveStakingList")
	public WebAsyncTask<RespPage<AliveStakingListResp>> aliveStakingList(@Valid @RequestBody AliveStakingListReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<AliveStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.aliveStakingList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/historyStakingList")
	public WebAsyncTask<RespPage<HistoryStakingListResp>> historyStakingList(@Valid @RequestBody HistoryStakingListReq req) {
		// 5s钟没返回，则认为超时
		WebAsyncTask<RespPage<HistoryStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.historyStakingList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/stakingDetails")
	public WebAsyncTask<BaseResp<StakingDetailsResp>> stakingDetails(@Valid @RequestBody StakingDetailsReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<BaseResp<StakingDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.stakingDetails(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/stakingOptRecordList")
	public WebAsyncTask<RespPage<StakingOptRecordListResp>> stakingOptRecordList(@Valid @RequestBody StakingOptRecordListReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<StakingOptRecordListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.stakingOptRecordList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/delegationListByStaking")
	public WebAsyncTask<RespPage<DelegationListByStakingResp>> delegationListByStaking(@Valid @RequestBody DelegationListByStakingReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<DelegationListByStakingResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.delegationListByStaking(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/delegationListByAddress")
	public WebAsyncTask<RespPage<DelegationListByAddressResp>> delegationListByAddress(@Valid @RequestBody DelegationListByAddressReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<DelegationListByAddressResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.delegationListByAddress(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "staking/lockedStakingList")
	public WebAsyncTask<RespPage<LockedStakingListResp>> lockedStakingList(@Valid @RequestBody LockedStakingListReq req) {
		/**
		 * 异步调用，超时则进入timeout
		 */
		WebAsyncTask<RespPage<LockedStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.lockedStakingList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}
}
