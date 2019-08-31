package com.platon.browser.controller;

import javax.validation.Valid;

import com.platon.browser.now.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;

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

	@Override
	public BaseResp<QueryDetailResp> details(@Valid QueryDetailRequest req) {
		return addressService.getDetails(req);
	}

}
