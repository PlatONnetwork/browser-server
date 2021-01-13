package com.platon.browser.controller;

import com.platon.browser.config.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenHolderListReq;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryHolderTokenListResp;
import com.platon.browser.response.token.QueryTokenHolderListResp;
import com.platon.browser.response.token.QueryTokenTransferRecordListResp;
import com.platon.browser.service.OldErc20TxService;
import com.platon.browser.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
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
public class Erc20TokenTransferRecordController {

    @Resource
    private OldErc20TxService oldErc20TxService;
    @Resource
    private DownFileCommon downFileCommon;
    @Resource
    private CommonMethod commonMethod;
    @Resource
    private I18nUtil i18n;

    @PostMapping(value = "token/tokenTransferList")
    public WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> tokenTransferList(@Valid @RequestBody QueryTokenTransferRecordListReq req) {
        WebAsyncTask<RespPage<QueryTokenTransferRecordListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                this.oldErc20TxService.queryTokenRecordList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @GetMapping(value = "token/exportTokenTransferList")
    public void exportTokenTransferList(
            @RequestParam(value = "address",required = false) String address,
            @RequestParam(value = "contract",required = false) String contract,
            @RequestParam(value = "date", required = true) Long date,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        try {
            /**
             * 鉴权
             */
            this.commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = this.oldErc20TxService.exportTokenTransferList(address, contract, date, local, timeZone, token, response);
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            this.log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @PostMapping(value = "token/tokenHolderList")
    public WebAsyncTask<RespPage<QueryTokenHolderListResp>> tokenHolderList(@Valid @RequestBody QueryTokenHolderListReq req) {
        WebAsyncTask<RespPage<QueryTokenHolderListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                this.oldErc20TxService.tokenHolderList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @GetMapping(value = "token/exportTokenHolderList")
    public void exportTokenHolderList(
            @RequestParam(value = "contract",required = true) String contract,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        try {
            /**
             * 鉴权
             */
            this.commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = this.oldErc20TxService.exportTokenHolderList(contract, local, timeZone, token, response);
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            this.log.error(e.getMessage());
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @PostMapping(value = "token/holderTokenList")
    public WebAsyncTask<RespPage<QueryHolderTokenListResp>> holderTokenList(@Valid @RequestBody QueryHolderTokenListReq req) {
        WebAsyncTask<RespPage<QueryHolderTokenListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () ->
                this.oldErc20TxService.holderTokenList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @GetMapping(value = "token/exportHolderTokenList")
    public void exportHolderTokenList(
            @RequestParam(value = "address",required = true) String address,
            @RequestParam(value = "local",required = true) String local,
            @RequestParam(value = "timeZone",required = true) String timeZone,
            @RequestParam(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        try {
            /**
             * 鉴权
             */
            this.commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = this.oldErc20TxService.exportHolderTokenList(address, local, timeZone, token, response);
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            this.log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }
}
