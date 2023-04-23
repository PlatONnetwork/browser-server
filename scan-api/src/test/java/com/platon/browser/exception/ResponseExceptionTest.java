package com.platon.browser.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ResponseExceptionTest {

	@Test
	public void test_PushChainStatisticNew() {
		ResponseException responseException = new ResponseException("123");
		assertEquals(responseException.getMessage(), "123");
	}

}
