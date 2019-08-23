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
	
}
