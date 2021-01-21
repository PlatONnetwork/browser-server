package com.platon.browser.controller.token;//package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.token.QueryTokenDetailReq;
import com.platon.browser.request.token.QueryTokenListReq;
import com.platon.browser.response.BaseResp;
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
@RequestMapping("token/arc20-tx")
public class Arc20TxController {
    @Resource
    private I18nUtil i18n;

    @PostMapping( "list")
    public Mono<BaseResp<String>> list(@Valid @RequestBody QueryTokenDetailReq req) {
        return Mono.create(sink -> {
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),""));
        });
    }

    @PostMapping( "export")
    public Mono<BaseResp<String>> export(@Valid @RequestBody QueryTokenListReq req) {
        return Mono.create(sink -> {
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),""));
        });
    }
}
