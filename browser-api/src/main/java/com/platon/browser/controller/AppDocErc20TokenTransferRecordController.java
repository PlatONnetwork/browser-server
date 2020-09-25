package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.now.service.Erc20TokenTransferRecordService;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Erc20TokenTransferRecordService erc20TokenTransferRecordService;

    @Override
    public WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@Valid QueryTokenTransferRecordListReq req) {
        WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                erc20TokenTransferRecordService.queryTokenRecordList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }
}
