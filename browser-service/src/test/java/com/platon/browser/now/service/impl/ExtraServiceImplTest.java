package com.platon.browser.now.service.impl;


import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.now.service.ExtraService;


public class ExtraServiceImplTest extends TestBase{

	@Autowired
    private ExtraService extraService;
	
	@Test
	public void getDetails() {
		assertNotNull(extraService.queryConfig());
	}
	
}
