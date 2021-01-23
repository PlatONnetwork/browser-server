package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenIdDetailReq;
import com.platon.browser.request.token.QueryTokenIdListReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.token.QueryTokenDetailResp;
import com.platon.browser.response.token.QueryTokenIdDetailResp;
import com.platon.browser.response.token.QueryTokenIdListResp;
import com.platon.browser.response.token.QueryTokenListResp;
import com.platon.browser.service.TokenService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class TokenController {

    @Resource
    private TokenService tokenService;
    @Resource
    private I18nUtil i18n;

    @PostMapping( "token/tokenDetail")
    public Mono<BaseResp<QueryTokenDetailResp>> tokenDetail(@Valid @RequestBody QueryTokenDetailReq req) {
        return Mono.create(sink -> {
            QueryTokenDetailResp resp = tokenService.queryTokenDetail(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),resp));
        });
    }

    @PostMapping( "token/tokenList")
    public Mono<RespPage<QueryTokenListResp>> tokenList(@Valid @RequestBody QueryTokenListReq req) {
        return Mono.just(tokenService.queryTokenList(req));
    }

    @PostMapping( "token/tokenId721List")
    public Mono<RespPage<QueryTokenIdListResp>> tokenId721List(@Valid @RequestBody QueryTokenIdListReq req) {
        return Mono.just(tokenService.queryTokenList(req));
    }

    @PostMapping( "token/tokenId721Detail")
    public Mono<BaseResp<QueryTokenIdDetailResp>> tokenId721Detail(@Valid @RequestBody QueryTokenIdDetailReq req) {
        return Mono.create(sink -> {
            QueryTokenIdDetailResp resp = tokenService.queryTokenIdDetail(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),resp));
        });
    }
}
