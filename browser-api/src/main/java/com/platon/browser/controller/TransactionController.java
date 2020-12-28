package com.platon.browser.controller;

import com.platon.browser.config.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.reqest.PageReq;
import com.platon.browser.reqest.newtransaction.TransactionDetailsReq;
import com.platon.browser.reqest.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.reqest.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.reqest.staking.QueryClaimByStakingReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.staking.QueryClaimByStakingResp;
import com.platon.browser.response.transaction.QueryClaimByAddressResp;
import com.platon.browser.response.transaction.TransactionDetailsResp;
import com.platon.browser.response.transaction.TransactionListResp;
import com.platon.browser.service.TransactionService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 交易模块Contract。定义使用方法
 * 
 * @file AppDocTransactionController.java
 * @description
 * @author zhangrj
 * @data 2019年8月31日
 */
@RestController
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Resource
    private I18nUtil i18n;
    @Resource
    private TransactionService transactionService;
    @Resource
    private DownFileCommon downFileCommon;
    @Resource
    private CommonMethod commonMethod;

    @PostMapping(value = "transaction/transactionList")
    public WebAsyncTask<RespPage<TransactionListResp>> transactionList(@Valid @RequestBody PageReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.getTransactionList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @PostMapping(value = "transaction/transactionListByBlock")
    public WebAsyncTask<RespPage<TransactionListResp>> transactionListByBlock(@Valid @RequestBody TransactionListByBlockRequest req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.getTransactionListByBlock(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @PostMapping(value = "transaction/transactionListByAddress")
    public WebAsyncTask<RespPage<TransactionListResp>> transactionListByAddress(@Valid @RequestBody TransactionListByAddressRequest req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
            () -> this.transactionService.getTransactionListByAddress(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @GetMapping(value = "transaction/addressTransactionDownload")
    public void addressTransactionDownload(
            @RequestParam(value = "address",required = false) String address,
            @RequestParam(value = "date", required = true) Long date,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        /**
         * 鉴权
         */
        this.commonMethod.recaptchaAuth(token);
        /**
         * 对地址进行补充前缀
         */
        address = address.toLowerCase();
        AccountDownload accountDownload =
            this.transactionService.transactionListByAddressDownload(address, date, local, timeZone);
        try {
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                accountDownload.getData());
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @PostMapping(value = "transaction/transactionDetails")
    public WebAsyncTask<BaseResp<TransactionDetailsResp>> transactionDetails(@Valid @RequestBody TransactionDetailsReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<BaseResp<TransactionDetailsResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
                TransactionDetailsResp transactionDetailsResp = this.transactionService.transactionDetails(req);
                return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), this.i18n.i(I18nEnum.SUCCESS),
                    transactionDetailsResp);
            });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @PostMapping(value = "transaction/queryClaimByAddress")
    public WebAsyncTask<RespPage<QueryClaimByAddressResp>> queryClaimByAddress(@Valid @RequestBody TransactionListByAddressRequest req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<QueryClaimByAddressResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.queryClaimByAddress(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @PostMapping(value = "transaction/queryClaimByStaking")
    public WebAsyncTask<RespPage<QueryClaimByStakingResp>> queryClaimByStaking(@Valid @RequestBody QueryClaimByStakingReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<QueryClaimByStakingResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.queryClaimByStaking(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }
}
