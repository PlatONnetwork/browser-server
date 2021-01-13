package com.platon.browser.controller;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.request.address.QueryDetailRequest;
import com.platon.browser.request.address.QueryRPPlanDetailRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.address.QueryDetailResp;
import com.platon.browser.response.address.QueryRPPlanDetailResp;
import com.platon.browser.service.AddressService;
import com.platon.browser.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 *          地址具体实现Controller 提供地址详情页面使用
 *  @file AppDocAddressController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Slf4j
@RestController
public class AddressController {

	@Resource
	private AddressService addressService;
    @Resource
    private I18nUtil i18n;

    @PostMapping( "address/details")
	public Mono<BaseResp<QueryDetailResp>> details(@Valid @RequestBody QueryDetailRequest req) {
        return Mono.create(sink -> {
            QueryDetailResp resp = addressService.getDetails(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
	}

    @PostMapping("address/rpplanDetail")
	public Mono<BaseResp<QueryRPPlanDetailResp>> rpplanDetail(@Valid @RequestBody QueryRPPlanDetailRequest req) {
        return Mono.create(sink -> {
            QueryRPPlanDetailResp resp = addressService.rpplanDetail(req);
            sink.success(BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp));
        });
	}
}
