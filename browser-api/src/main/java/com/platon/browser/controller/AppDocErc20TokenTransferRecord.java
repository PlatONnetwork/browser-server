package com.platon.browser.controller;

import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

@Api(value = "合约内部交易接口模块", tags = "Token")
public interface AppDocErc20TokenTransferRecord {

    @ApiOperation(value = "token/tokenTransferList", nickname = "token list",
            notes = "查询合约token转账交易列表", response = QueryTokenTransferRecordListResp.class, tags = {"Token"})
    @PostMapping(value = "token/tokenTransferList", produces = {"application/json"})
    WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@ApiParam(value = "QueryTokenTransferRecordListReq ", required = true)
                                                         @Valid @RequestBody QueryTokenTransferRecordListReq req);
}
