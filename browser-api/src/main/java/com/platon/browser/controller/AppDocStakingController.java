package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

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
	
	@Override
	public WebAsyncTask<BaseResp<StakingStatisticNewResp>> stakingStatisticNew() {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<StakingStatisticNewResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<BaseResp<StakingStatisticNewResp>>() {  
            @Override  
            public BaseResp<StakingStatisticNewResp> call() throws Exception {  
            	StakingStatisticNewResp stakingStatisticNewResp = stakingService.stakingStatisticNew();
        		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),stakingStatisticNewResp);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<BaseResp<StakingStatisticNewResp>>() {  
            @Override  
            public BaseResp<StakingStatisticNewResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<AliveStakingListResp>> aliveStakingList(@Valid AliveStakingListReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<AliveStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<AliveStakingListResp>>() {  
            @Override  
            public RespPage<AliveStakingListResp> call() throws Exception {  
            	return stakingService.aliveStakingList(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<AliveStakingListResp>>() {  
            @Override  
            public RespPage<AliveStakingListResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<HistoryStakingListResp>> historyStakingList(@Valid HistoryStakingListReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<HistoryStakingListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<HistoryStakingListResp>>() {  
            @Override  
            public RespPage<HistoryStakingListResp> call() throws Exception {  
            	return stakingService.historyStakingList(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<HistoryStakingListResp>>() {  
            @Override  
            public RespPage<HistoryStakingListResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<BaseResp<StakingDetailsResp>> stakingDetails(@Valid StakingDetailsReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<StakingDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<BaseResp<StakingDetailsResp>>() {  
            @Override  
            public BaseResp<StakingDetailsResp> call() throws Exception {  
            	return stakingService.stakingDetails(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<BaseResp<StakingDetailsResp>>() {  
            @Override  
            public BaseResp<StakingDetailsResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<StakingOptRecordListResp>> stakingOptRecordList(@Valid StakingOptRecordListReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<StakingOptRecordListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<StakingOptRecordListResp>>() {  
            @Override  
            public RespPage<StakingOptRecordListResp> call() throws Exception {  
            	return stakingService.stakingOptRecordList(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<StakingOptRecordListResp>>() {  
            @Override  
            public RespPage<StakingOptRecordListResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<DelegationListByStakingResp>> delegationListByStaking(@Valid DelegationListByStakingReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<DelegationListByStakingResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<DelegationListByStakingResp>>() {  
            @Override  
            public RespPage<DelegationListByStakingResp> call() throws Exception {  
            	return stakingService.delegationListByStaking(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<DelegationListByStakingResp>>() {  
            @Override  
            public RespPage<DelegationListByStakingResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<DelegationListByAddressResp>> delegationListByAddress(@Valid DelegationListByAddressReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<DelegationListByAddressResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<DelegationListByAddressResp>>() {  
            @Override  
            public RespPage<DelegationListByAddressResp> call() throws Exception {  
            	return stakingService.delegationListByAddress(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<DelegationListByAddressResp>>() {  
            @Override  
            public RespPage<DelegationListByAddressResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
	}

}
