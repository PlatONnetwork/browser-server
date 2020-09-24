package com.platon.browser.controller;

import com.platon.browser.req.token.QueryTokenDetailReq;
import com.platon.browser.req.token.QueryTokenListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenDetailResp;
import com.platon.browser.res.token.QueryTokenListResp;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

@RestController
public class AppDocErc20TokenController implements AppDocErc20Token {

    @Override
    public WebAsyncTask<BaseResp<QueryTokenDetailResp>> tokenDetail(@Valid QueryTokenDetailReq req) {
        return null;
    }

    @Override
    public WebAsyncTask<RespPage<QueryTokenListResp>> tokenList(@Valid QueryTokenListReq req) {
        return null;
    }
}
