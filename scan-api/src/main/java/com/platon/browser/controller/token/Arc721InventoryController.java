package com.platon.browser.controller.token;//package com.platon.browser.controller;

import com.platon.browser.config.CommonMethod;
import com.platon.browser.config.DownFileCommon;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.request.token.QueryTokenIdDetailReq;
import com.platon.browser.request.token.QueryTokenIdListReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.account.AccountDownload;
import com.platon.browser.response.token.QueryTokenIdDetailResp;
import com.platon.browser.response.token.QueryTokenIdListResp;
import com.platon.browser.service.TokenService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("token/arc721-inventory")
public class Arc721InventoryController {

    @Resource
    private I18nUtil i18n;

    @Resource
    private DownFileCommon downFileCommon;

    @Resource
    private CommonMethod commonMethod;

    @Resource
    private TokenService tokenService;

    /**
     * ARC721 库存列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.token.QueryTokenIdListResp>>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/1/28
     */
    @PostMapping("list")
    public Mono<RespPage<QueryTokenIdListResp>> list(@Valid @RequestBody QueryTokenIdListReq req) {
        return Mono.just(tokenService.queryTokenIdList(req));
    }

    /**
     * ARC721库存详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.token.QueryTokenIdDetailResp>>
     * @date 2021/5/25
     */
    @PostMapping("detail")
    public Mono<BaseResp<QueryTokenIdDetailResp>> detail(@Valid @RequestBody QueryTokenIdDetailReq req) {
        return Mono.create(sink -> {
            QueryTokenIdDetailResp resp = tokenService.queryTokenIdDetail(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

    /**
     * ARC721库存列表导出
     *
     * @param address
     * @param contract
     * @param tokenId
     * @param local
     * @param timeZone
     * @param token
     * @param response
     * @return void
     * @date 2021/5/25
     */
    @GetMapping("export")
    public void export(@RequestParam(value = "address", required = false) String address,
                       @RequestParam(value = "contract", required = false) String contract,
                       @RequestParam(value = "tokenId", required = false) String tokenId,
                       @RequestParam(value = "local", required = true) String local,
                       @RequestParam(value = "timeZone", required = true) String timeZone,
                       @RequestParam(value = "token", required = false) String token,
                       HttpServletResponse response) {
        try {
            /**
             * 鉴权
             */
            commonMethod.recaptchaAuth(token);
            AccountDownload accountDownload = tokenService.exportTokenId(address, contract, tokenId, local, timeZone);
            downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                    accountDownload.getData());
        } catch (Exception e) {
            log.error("download error", e);
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

}
