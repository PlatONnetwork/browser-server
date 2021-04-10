package com.platon.browser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.config.MessageDto;

@RunWith(SpringRunner.class)
public class MessageDtoTest {

	private MessageDto messageDto = new MessageDto();
	
	@Before
	public void setUp() {
		messageDto.analysisKey("1|10|1|all");
	}
	
	@Test
	public void test_analysisData() {
		assertNotNull(messageDto.analysisData("1,1,10,all,1"));
	}
	
	@Test
	public void test_analysisKey() {
		assertNotNull(messageDto.analysisKey("1|10|1|all"));
	}
	
	@Test
	public void test_getMessageKey() {
		assertEquals(messageDto.getMessageKey(), "1|10|1|all");
	}
	
}
