package com.platon.browser.controller;

import com.platon.browser.config.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.response.home.BlockStatisticNewResp;
import com.platon.browser.response.home.ChainStatisticNewResp;
import com.platon.browser.response.home.QueryNavigationResp;
import com.platon.browser.response.home.StakingListNewResp;
import com.platon.browser.service.HomeService;
import com.platon.browser.request.home.QueryNavigationRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.util.I18nUtil;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 *  首页模块Contract。定义使用方法
 *  @file AppDocHomeController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class HomeController {

	@Resource
    private I18nUtil i18n;
	@Resource
    private HomeService homeService;

    @PostMapping(value = "home/queryNavigation", produces = { "application/json" })
	public WebAsyncTask<BaseResp<QueryNavigationResp>> queryNavigation(@Valid @RequestBody QueryNavigationRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<QueryNavigationResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
             try{
                 QueryNavigationResp queryNavigationResp = homeService.queryNavigation(req);
                 return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),queryNavigationResp);
             }catch (BusinessException be){
                 throw new ResponseException(be.getErrorMessage());
             }
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

    @SubscribeMapping(value = "topic/block/statistic/new")
    @PostMapping(value = "home/blockStatistic", produces = { "application/json" })
	public WebAsyncTask<BaseResp<BlockStatisticNewResp>> blockStatisticNew() {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<BlockStatisticNewResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            BlockStatisticNewResp blockStatisticNewResp = homeService.blockStatisticNew();
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockStatisticNewResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

    @SubscribeMapping(value = "/topic/chain/statistic/new")
    @PostMapping(value = "home/chainStatistic", produces = { "application/json" })
	public WebAsyncTask<BaseResp<ChainStatisticNewResp>> chainStatisticNew() {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<ChainStatisticNewResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            ChainStatisticNewResp chainStatisticNewResp = homeService.chainStatisticNew();
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),chainStatisticNewResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

    @SubscribeMapping(value = "topic/staking/list/new")
    @PostMapping(value = "home/stakingList", produces = { "application/json" })
	public WebAsyncTask<BaseResp<StakingListNewResp>> stakingListNew() {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<StakingListNewResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            StakingListNewResp stakingListNewResp = homeService.stakingListNew();
            /**
             * 第一次返回都设为true
             */
            stakingListNewResp.setIsRefresh(true);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),stakingListNewResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	
	}

}
