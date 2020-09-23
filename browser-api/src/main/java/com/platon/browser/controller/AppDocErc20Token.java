package com.platon.browser.controller;

import com.platon.browser.req.token.QueryTokenDetailReq;
import com.platon.browser.req.token.QueryTokenListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenDetailResp;
import com.platon.browser.res.token.QueryTokenListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

/**
 * Erc20-Token 合约模块
 *
 * @author RJ
 */
@Api(value = "/token", tags = "Token")
public interface AppDocErc20Token {

    @ApiOperation(value = "address/details", nickname = "token details",
            notes = "查询合约地址详情", response = QueryTokenDetailResp.class, tags = {"Token"})
    @PostMapping(value = "address/tokenDetail", produces = {"application/json"})
    WebAsyncTask<BaseResp<QueryTokenDetailResp>> tokenDetail(@ApiParam(value = "QueryDetailReq ", required = true)
                                                    @Valid @RequestBody QueryTokenDetailReq req);

    @ApiOperation(value = "address/tokenList", nickname = "token list",
            notes = "查询合约列表", response = QueryTokenListResp.class, tags = {"Token"})
    @PostMapping(value = "address/tokenList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryTokenListResp>> tokenList(@ApiParam(value = "QueryTokenListReq ", required = true)
                                                         @Valid @RequestBody QueryTokenListReq req);
}
