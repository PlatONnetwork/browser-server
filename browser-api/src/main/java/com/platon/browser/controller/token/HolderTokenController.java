package com.platon.browser.controller.token;//package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.token.QueryHolderTokenListReq;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryHolderTokenListResp;
import com.platon.browser.service.ErcTxService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("token/holder-token")
public class HolderTokenController {
    @Resource
    private I18nUtil i18n;
    @Resource
    private ErcTxService ercTxService;
    @Resource
    private DownFileCommon downFileCommon;
    @Resource
    private CommonMethod commonMethod;

    @PostMapping( "list")
    public Mono<RespPage<QueryHolderTokenListResp>> list(@Valid @RequestBody QueryHolderTokenListReq req) {
        return Mono.just(ercTxService.holderTokenList(req));
    }

    @PostMapping( "export")
    public void export(@RequestParam(value = "address",required = true) String address,
                                         @RequestParam(value = "local",required = true) String local,
                                         @RequestParam(value = "timeZone",required = true) String timeZone,
                                         @RequestParam(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = ercTxService.exportHolderTokenList(address, local, timeZone);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error("download error", e);
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }
}
