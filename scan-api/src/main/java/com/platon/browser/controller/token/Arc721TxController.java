package com.platon.browser.controller.token;//package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.token.QueryTokenTransferRecordListReq;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryTokenTransferRecordListResp;
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
@RequestMapping("token/arc721-tx")
public class Arc721TxController {

    @Resource
    private I18nUtil i18n;

    @Resource
    private ErcTxService ercTxService;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private CommonMethod commonMethod;

    /**
     * ARC721交易列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.token.QueryTokenTransferRecordListResp>>
     * @date 2021/1/28
     */
    @PostMapping("list")
    public Mono<RespPage<QueryTokenTransferRecordListResp>> list(@Valid @RequestBody QueryTokenTransferRecordListReq req) {
        return Mono.just(ercTxService.token721TransferList(req));
    }

    /**
     * arc721交易列表导出
     *
     * @param address  钱包地址
     * @param contract 合约地址
     * @param date     开始日期时间戳
     * @param local    区域：en或者zh-cn
     * @param timeZone 时区
     * @param token    令牌
     * @param tokenId  token id
     * @param response
     * @return void
     * @date 2021/1/27
     */
    @GetMapping("export")
    public void export(@RequestParam(value = "address", required = false) String address,
                       @RequestParam(value = "contract", required = false) String contract,
                       @RequestParam(value = "date", required = true) Long date,
                       @RequestParam(value = "local", required = true) String local,
                       @RequestParam(value = "timeZone", required = true) String timeZone,
                       @RequestParam(value = "token", required = false) String token,
                       @RequestParam(value = "tokenId", required = false) String tokenId,
                       HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = ercTxService.exportToken721TransferList(address, contract, date, local, timeZone, tokenId);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

}
