package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.extra.QueryConfigResp;
import com.platon.browser.service.ExtraService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * controller实现
 *  @file AppDocExtraController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月25日
 */
@Slf4j
@RestController
public class ExtraController {

	@Resource
	private ExtraService extraService;
	@Resource
    private I18nUtil i18n;

	@PostMapping("extra/queryConfig")
	public Mono<BaseResp<QueryConfigResp>> queryConfig() {
		return Mono.create(sink -> {
			QueryConfigResp resp = extraService.queryConfig();
			sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),resp));
		});
	}
}
