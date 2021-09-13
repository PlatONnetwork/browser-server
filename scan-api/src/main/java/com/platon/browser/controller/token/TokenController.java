package com.platon.browser.controller.token;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.token.QueryTokenDetailResp;
import com.platon.browser.response.token.QueryTokenListResp;
import com.platon.browser.service.TokenService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("token")
public class TokenController {

    @Resource
    private TokenService tokenService;

    @Resource
    private I18nUtil i18n;

    /**
     * token令牌列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.token.QueryTokenListResp>>
     * @date 2021/1/28
     */
    @PostMapping("list")
    public Mono<RespPage<QueryTokenListResp>> list(@Valid @RequestBody QueryTokenListReq req) {
        return Mono.create(sink -> {
            RespPage<QueryTokenListResp> resp = tokenService.queryTokenList(req);
            sink.success(resp);
        });
    }

    /**
     * Token令牌详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.token.QueryTokenDetailResp>>
     * @date 2021/1/28
     */
    @PostMapping("detail")
    public Mono<BaseResp<QueryTokenDetailResp>> detail(@Valid @RequestBody QueryTokenDetailReq req) {
        return Mono.create(sink -> {
            QueryTokenDetailResp resp = tokenService.queryTokenDetail(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
    }

}
