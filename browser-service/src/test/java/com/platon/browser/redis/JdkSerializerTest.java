package com.platon.browser.redis;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JdkSerializerTest {

	@Test
	public void test() {
		JdkSerializer<Object> a = new JdkSerializer<>();
		List<Map<String, Integer>> serialize = new ArrayList<>();
		Map<String, Integer> m = new HashMap<>();
		m.put("A", 123);
		serialize.add(m);
		byte[] str = a.serialize(serialize);
		Object deserialize = a.deserialize(str);
		assertNotNull(deserialize);
	}
	
}
