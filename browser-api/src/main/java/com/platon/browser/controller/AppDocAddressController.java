package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.address.QueryDetailResp;

@RestController
public class AppDocAddressController implements AppDocAddress {

	@Override
	public BaseResp<QueryDetailResp> details(@Valid QueryDetailRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
