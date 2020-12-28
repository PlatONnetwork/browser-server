package com.platon.browser.dao.param;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BusinessParamTest extends TestBase {

	@Test
	public void test(){
		BusinessParam.YesNoEnum yesNoEnum = BusinessParam.YesNoEnum.NO;
		yesNoEnum.getCode();
		yesNoEnum.getDesc();
		assertTrue(true);
	}
}
