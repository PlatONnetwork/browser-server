package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.req.staking.QueryClaimByStakingReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.QueryClaimByStakingResp;
import com.platon.browser.res.transaction.QueryClaimByAddressResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.utils.HexTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 交易模块Contract。定义使用方法
 *  @file AppDocTransactionController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class AppDocTransactionController implements AppDocTransaction {

	private final Logger logger = LoggerFactory.getLogger(AppDocTransactionController.class);

	@Autowired
	private I18nUtil i18n;

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private DownFileCommon downFileCommon;
	
	@Autowired
	private CommonMethod commonMethod;

	@Override
	public WebAsyncTask<RespPage<TransactionListResp>> transactionList(@Valid PageReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> transactionService.getTransactionList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<TransactionListResp>> transactionListByBlock(@Valid TransactionListByBlockRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> transactionService.getTransactionListByBlock(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<TransactionListResp>> transactionListByAddress(@Valid TransactionListByAddressRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> transactionService.getTransactionListByAddress(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@Override
	public void addressTransactionDownload(String address, Long date, String local, String timeZone, String token, HttpServletResponse response) {
		/**
		 * 鉴权
		 */
		commonMethod.recaptchaAuth(token);
		/**
		 * 对地址进行补充前缀
		 */
		address = HexTool.prefix(address.toLowerCase());
		AccountDownload accountDownload = transactionService.transactionListByAddressDownload(address, date, local, timeZone);
		try {
			downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(), accountDownload.getData());
		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
		}
	}

	@Override
	public WebAsyncTask<BaseResp<TransactionDetailsResp>> transactionDetails(@Valid TransactionDetailsReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<BaseResp<TransactionDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            TransactionDetailsResp transactionDetailsResp = transactionService.transactionDetails(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactionDetailsResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<QueryClaimByAddressResp>> queryClaimByAddress(
			@Valid TransactionListByAddressRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<QueryClaimByAddressResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> 
            transactionService.queryClaimByAddress(req)
        );
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<QueryClaimByStakingResp>> queryClaimByStaking(@Valid QueryClaimByStakingReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
        WebAsyncTask<RespPage<QueryClaimByStakingResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> 
            transactionService.queryClaimByStaking(req)
        );
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask; 
	}

}
