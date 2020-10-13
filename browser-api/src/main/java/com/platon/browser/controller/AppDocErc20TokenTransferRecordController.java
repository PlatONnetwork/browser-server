package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.Erc20TokenTransferRecordService;
import com.platon.browser.req.token.QueryHolderTokenListReq;
import com.platon.browser.req.token.QueryTokenHolderListReq;
import com.platon.browser.req.token.QueryTokenTransferRecordListReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.token.QueryHolderTokenListResp;
import com.platon.browser.res.token.QueryTokenHolderListResp;
import com.platon.browser.res.token.QueryTokenTransferRecordListResp;
import com.platon.browser.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 合约内部交易模块Controller
 *
 * @author AgentRJ
 * @create 2020-09-23 15:04
 */
@Slf4j
@RestController
public class AppDocErc20TokenTransferRecordController implements AppDocErc20TokenTransferRecord {

    @Autowired
    private Erc20TokenTransferRecordService erc20TokenTransferRecordService;

    @Autowired
    private DownFileCommon downFileCommon;

    @Autowired
    private CommonMethod commonMethod;

    @Autowired
    private I18nUtil i18n;

    @Override
    public WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@Valid QueryTokenTransferRecordListReq req) {
        WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                this.erc20TokenTransferRecordService.queryTokenRecordList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public void exportTokenTransferList(String address, String contract, Long date, String local, String timeZone, String token, HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            this.commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = this.erc20TokenTransferRecordService.exportTokenTransferList(address, contract, date, local, timeZone, token, response);
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            this.log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @Override
    public WebAsyncTask<RespPage<QueryTokenHolderListResp>> tokenHolderList(@Valid QueryTokenHolderListReq req) {
        WebAsyncTask<RespPage<QueryTokenHolderListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                this.erc20TokenTransferRecordService.tokenHolderList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public void exportTokenHolderList(String contract, String local, String timeZone, String token, HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            this.commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = this.erc20TokenTransferRecordService.exportTokenHolderList(contract, local, timeZone, token, response);
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            this.log.error(e.getMessage());
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @Override
    public WebAsyncTask<RespPage<QueryHolderTokenListResp>> holderTokenList(@Valid QueryHolderTokenListReq req) {
        WebAsyncTask<RespPage<QueryHolderTokenListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                this.erc20TokenTransferRecordService.holderTokenList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public void exportHolderTokenList(String address, String local, String timeZone, String token, HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            this.commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = this.erc20TokenTransferRecordService.exportHolderTokenList(address, local, timeZone, token, response);
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            this.log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }
}
