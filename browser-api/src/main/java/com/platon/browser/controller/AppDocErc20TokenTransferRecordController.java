package com.platon.browser.controller;

import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

/**
 * 合约内部交易模块Controller
 *
 * @author AgentRJ
 * @create 2020-09-23 15:04
 */
@RestController
public class AppDocErc20TokenTransferRecordController implements AppDocErc20TokenTransferRecord {

    @Override
    public WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@Valid QueryTokenTransferRecordListReq req) {
        return null;
    }
}
