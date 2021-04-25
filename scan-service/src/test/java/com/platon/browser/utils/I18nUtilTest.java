//package com.platon.browser.util;
//
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Spy;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import com.platon.browser.enums.I18nEnum;
//import com.platon.browser.util.I18nUtil;
//
//import static org.mockito.Mockito.*;
//
//import java.util.Locale;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(LocaleContextHolder.class)
//public class I18nUtilTest {
//
//	@Spy
//	private I18nUtil i18nUtil;
//
//	@Mock
//	private MessageSource messageSource;
//
//	@Before
//	public void setup() throws Exception {
//		MockitoAnnotations.initMocks(this);
//		Locale locale = new Locale("CN");
////		LocaleContextHolder localeContextHolder = PowerMockito.mock(LocaleContextHolder.class);
////		PowerMockito.whenNew(LocaleContextHolder.class).withAnyArguments().thenReturn(localeContextHolder);
//		PowerMockito.mockStatic(LocaleContextHolder.class);
//		PowerMockito.when(LocaleContextHolder.getLocale()).thenReturn(locale);
//		ReflectionTestUtils.setField(i18nUtil,"messageSource",messageSource);
//		when(messageSource.getMessage(any(),anyObject(), anyObject())).thenReturn("成功");
//	}
//
//	@Test
//	public void testII18nEnumObjectArray() {
//		Object[] params = {};
//		String msg = i18nUtil.i(I18nEnum.SUCCESS, params);
//		assertTrue("成功".equals(msg));
//	}
//
//	@Test
//	public void testII18nEnumStringObjectArray() {
//		// CN
//		Object[] params = {};
//		String msg = i18nUtil.i(I18nEnum.SUCCESS, "CN", params);
//		assertTrue("成功".equals(msg));
//	}
//
//	@Test
//	public void testGetMessageForStr() {
//		Object[] params = {};
//		String msg = i18nUtil.getMessageForStr("SUCCESS", "CN", params);
//		assertTrue("成功".equals(msg));
//	}
//
//}
