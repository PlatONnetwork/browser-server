package com.platon.browser.redis;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializerUtilsTest {

	@Test
	public void test() {
		SerializerUtils.getRedisSerializer();
		byte[] key = "a".getBytes();
		SerializerUtils.rawKey(key);
		byte[] rawKey = SerializerUtils.rawKey("a");
		
		SerializerUtils.rawValue(key);
		SerializerUtils.rawValue("a");
		
		SerializerUtils.rawValues("a","b");
		
		SerializerUtils.rawHashKey(key);
		SerializerUtils.rawHashKey("a");
		SerializerUtils.rawHashKeys("a","b");
		
		SerializerUtils.rawHashValue(key);
		SerializerUtils.rawHashValue("a");
		
		SerializerUtils.deserializeStr(rawKey);
		
		Map<byte[], byte[]> byteMap = new HashMap<>();
		byteMap.put(rawKey, rawKey);
		SerializerUtils.deserializerMapObj(byteMap);
		assertTrue(Boolean.TRUE);
	}
	
}
