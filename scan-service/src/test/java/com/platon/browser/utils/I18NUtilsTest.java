package com.platon.browser.utils;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class I18NUtilsTest {

	@Test
	public void test() {
		I18NUtils.init("messages", "US");
		I18NUtils i18nUtils = I18NUtils.getInstance();
		assertNotNull(i18nUtils.getResource("success"));
		assertNull(i18nUtils.getResource(0));
	}

}
