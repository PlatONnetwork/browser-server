package com.platon.browser.controller.token;//package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.token.QueryHolderTokenListReq;
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

    /**
     * 持有者的Token令牌列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.token.QueryHolderTokenListResp>>
     * @date 2021/5/25
     */
    @PostMapping("list")
    public Mono<RespPage<QueryHolderTokenListResp>> list(@Valid @RequestBody QueryHolderTokenListReq req) {
        return Mono.just(ercTxService.holderTokenList(req));
    }

    /**
     * 持有者的token令牌导出
     *
     * @param address  合约地址
     * @param local    区域：en或者zh-cn
     * @param timeZone 时区
     * @param token    令牌
     * @param type     合约类型（取值erc20或erc721）
     * @param response
     * @return void
     * @date 2021/1/27
     */
    @GetMapping("export")
    public void export(@RequestParam(value = "address", required = true) String address,
                       @RequestParam(value = "local", required = true) String local,
                       @RequestParam(value = "timeZone", required = true) String timeZone,
                       @RequestParam(value = "token", required = false) String token,
                       @RequestParam(value = "type", required = false) String type,
                       HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = ercTxService.exportHolderTokenList(address, local, timeZone, type);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error("download error", e);
            throw new BusinessException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

}
