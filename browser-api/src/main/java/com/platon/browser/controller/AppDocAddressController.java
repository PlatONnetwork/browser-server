package com.platon.browser.controller;

import javax.validation.Valid;

import com.platon.browser.now.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;

@RestController
public class AppDocAddressController implements AppDocAddress {

	@Autowired
	private AddressService addressService;

	@Override
	public BaseResp<QueryDetailResp> details(@Valid QueryDetailRequest req) {
		return addressService.getDetails(req);
	}

}
