package com.platon.browser.controller;

import javax.validation.Valid;

import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;
import com.platon.browser.res.address.QueryRPPlanDetailResp;
import com.platon.browser.util.I18nUtil;

/**
 *          地址具体实现Controller 提供地址详情页面使用
 *  @file AppDocAddressController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class AppDocAddressController implements AppDocAddress {

	@Autowired
	private AddressService addressService;
	
    @Autowired
    private I18nUtil i18n;

	@Override
	public BaseResp<QueryDetailResp> details(@Valid QueryDetailRequest req) {
		QueryDetailResp queryDetailResp = addressService.getDetails(req);
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), queryDetailResp);
	}

	@Override
	public BaseResp<QueryRPPlanDetailResp> rpplanDetail(@Valid QueryRPPlanDetailRequest req) {
		QueryRPPlanDetailResp dueryRPPlanDetailResp = addressService.rpplanDetail(req);
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), dueryRPPlanDetailResp);
	}

}
