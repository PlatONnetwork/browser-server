package com.platon.browser.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.platon.browser.BrowserApiApplication;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class RedisPipelineToolTest {
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	@Before
	public void setUp() throws Exception {
		redisTemplate.boundValueOps("key1").set("{\"key1\":\"value1\"}");
		redisTemplate.boundValueOps("key2").set("{\"key2\":\"value2\"}");
		redisTemplate.boundValueOps("key3").set("{\"key3\":\"value3\"}");
	}

	@Test
	public void testBatchQueryByKeys() {
		List<String> keys = new ArrayList<String>();
		keys.add("key1");
		keys.add("key2");
		keys.add("key3");
		Map<String, Map> map = RedisPipelineTool.batchQueryByKeys(keys, false, Map.class, redisTemplate);
		assertNotNull(map);
	}

	@Test
	public void testBatchDeleteByKeys() {
		List<String> keys = new ArrayList<String>();
		keys.add("key1");
		keys.add("key2");
		keys.add("key3");
		Integer num = RedisPipelineTool.batchDeleteByKeys(keys, false, redisTemplate);
		assertTrue(num==3);
	}
	
	@After
	public void tearDown() throws Exception {
		redisTemplate.delete("key1");
		redisTemplate.delete("key2");
		redisTemplate.delete("key3");
	}
	

}
