package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.req.staking.*;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.*;
import com.platon.browser.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

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
public class AppDocStakingController implements AppDocStaking {

	@Autowired
	private I18nUtil i18n;

	@Autowired
	private StakingService stakingService;

	@Override
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

	@Override
	public WebAsyncTask<RespPage<AliveStakingListResp>> aliveStakingList(@Valid AliveStakingListReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<AliveStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.aliveStakingList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<RespPage<HistoryStakingListResp>> historyStakingList(@Valid HistoryStakingListReq req) {
		// 5s钟没返回，则认为超时
		WebAsyncTask<RespPage<HistoryStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.historyStakingList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<BaseResp<StakingDetailsResp>> stakingDetails(@Valid StakingDetailsReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<BaseResp<StakingDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.stakingDetails(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<RespPage<StakingOptRecordListResp>> stakingOptRecordList(@Valid StakingOptRecordListReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<StakingOptRecordListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.stakingOptRecordList(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<RespPage<DelegationListByStakingResp>> delegationListByStaking(
			@Valid DelegationListByStakingReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<DelegationListByStakingResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.delegationListByStaking(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<RespPage<DelegationListByAddressResp>> delegationListByAddress(
			@Valid DelegationListByAddressReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<DelegationListByAddressResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> stakingService.delegationListByAddress(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	
}
