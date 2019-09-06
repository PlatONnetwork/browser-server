package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.now.service.AddressService;
import com.platon.browser.req.address.QueryDetailRequest;
import com.platon.browser.req.address.QueryRPPlanDetailRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class AddressServiceImplTest {

	@Autowired
    private AddressService addressService;
	
	@Test
	public void getDetails() {
		QueryDetailRequest req = new QueryDetailRequest();
		req.setAddress("abcdefdsfdsa");
		assertNotNull(addressService.getDetails(req));
	}
	@Test
	public void rpplanDetail() {
		QueryRPPlanDetailRequest req = new QueryRPPlanDetailRequest();
		req.setPageNo(0);
		req.setPageSize(10);
		req.setAddress("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
		assertNotNull(addressService.rpplanDetail(req));
	}
	
}
