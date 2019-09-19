package com.platon.browser.exception;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

import com.platon.browser.enums.ErrorCodeEnum;

public class BusinessExceptionTest {

	@Test
	public void testBusinessExceptionString() {
		try {
			throw new BusinessException("BusinessException");
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);;
		}
	}

	@Test
	public void testBusinessExceptionStringLocale() {
		try {
			throw new BusinessException("BusinessException",Locale.getDefault());
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

	@Test
	public void testBusinessExceptionIntegerString() {
		try {
			throw new BusinessException(-1,"BusinessException");
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

	@Test
	public void testBusinessExceptionIntegerStringLocale() {
		try {
			throw new BusinessException(-1,"BusinessException",Locale.getDefault());
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

	@Test
	public void testBusinessExceptionErrorCodeEnum() {
		try {
			throw new BusinessException(ErrorCodeEnum.DEFAULT);
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

	@Test
	public void testBusinessExceptionErrorCodeEnumLocale() {
		try {
			throw new BusinessException(ErrorCodeEnum.DEFAULT,Locale.getDefault());
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

	@Test
	public void testBusinessExceptionIntegerStringThrowable() {
		try {
			throw new BusinessException(-1,"BusinessException", 
					new RuntimeException("RuntimeException"));
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

	@Test
	public void testBusinessExceptionIntegerStringLocaleThrowable() {
		try {
			throw new BusinessException(-1,"BusinessException",
					Locale.getDefault(),new RuntimeException("RuntimeException"));
		} catch (Exception e) {
			assertTrue(e instanceof BusinessException);
		}
	}

}
