package com.platon.browser.service.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;

public class RedisBlockServiceTest  extends TestBase{

	@Autowired
	private RedisBlockService redisBlockService;
	
	@Test
	public void testRedisBlock() {
		redisBlockService.getCacheKey();
	}
	
}
