package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.utils.HexTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.TimeoutException;

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

	@Override
	public WebAsyncTask<RespPage<TransactionListResp>> transactionList(@Valid PageReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> transactionService.getTransactionList(req));
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<TransactionListResp>> transactionListByBlock(@Valid TransactionListByBlockRequest req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> transactionService.getTransactionListByBlock(req));
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

	@Override
	public WebAsyncTask<RespPage<TransactionListResp>> transactionListByAddress(@Valid TransactionListByAddressRequest req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> transactionService.getTransactionListByAddress(req));
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

	@Override
	public void addressTransactionDownload(String address, Long date, String local, String timeZone, HttpServletResponse response) {
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
		// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<TransactionDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            TransactionDetailsResp transactionDetailsResp = transactionService.transactionDetails(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactionDetailsResp);
        });
        webAsyncTask.onCompletion(() -> {
        });
        webAsyncTask.onTimeout(() -> {
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("System busy!");
        });
        return webAsyncTask;  
	}

}
