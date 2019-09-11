package com.platon.browser.service.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.util.I18nUtil;

public class I18nUtilTest extends TestBase{
	
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
