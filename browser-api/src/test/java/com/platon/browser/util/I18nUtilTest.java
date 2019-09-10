package com.platon.browser.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.enums.I18nEnum;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class I18nUtilTest {
	
	@Autowired
	private I18nUtil i18nUtil;

	@Test
	public void testII18nEnumObjectArray() {
		Object[] params = {};
		String msg = i18nUtil.i(I18nEnum.SUCCESS, params);
		assertTrue("成功".equals(msg));
	}

	@Test
	public void testII18nEnumStringObjectArray() {
		// CN
		Object[] params = {};
		String msg = i18nUtil.i(I18nEnum.SUCCESS, "CN", params);
		assertTrue("成功".equals(msg));
	}

	@Test
	public void testGetMessageForStr() {
		Object[] params = {};
		String msg = i18nUtil.getMessageForStr("SUCCESS", "CN", params);
		assertTrue("成功".equals(msg));
	}

}
