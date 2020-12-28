package com.platon.browser.controller;

import com.platon.browser.config.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.reqest.token.QueryTokenDetailReq;
import com.platon.browser.reqest.token.QueryTokenListReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.token.QueryTokenDetailResp;
import com.platon.browser.response.token.QueryTokenListResp;
import com.platon.browser.service.Erc20TokenService;
import com.platon.browser.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Slf4j
public class Erc20TokenController {

    @Resource
    private Erc20TokenService erc20TokenService;
    @Resource
    private I18nUtil i18n;

    @PostMapping(value = "token/tokenDetail")
    public WebAsyncTask<BaseResp<QueryTokenDetailResp>> tokenDetail(@Valid @RequestBody QueryTokenDetailReq req) {
        WebAsyncTask<BaseResp<QueryTokenDetailResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            QueryTokenDetailResp queryDetailResp = erc20TokenService.queryTokenDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), queryDetailResp);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @PostMapping(value = "token/tokenList")
    public WebAsyncTask<RespPage<QueryTokenListResp>> tokenList(@Valid @RequestBody QueryTokenListReq req) {
        WebAsyncTask<RespPage<QueryTokenListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
            return erc20TokenService.queryTokenList(req);
        });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }
}
