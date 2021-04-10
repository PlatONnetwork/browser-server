//package com.platon.browser.config.redis;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.platon.browser.config.redis.JdkSerializer;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class JdkSerializerTest {
//
//	@Test
//	public void test() {
//		JdkSerializer<Object> a = new JdkSerializer<>();
//		List<Map<String, Integer>> serialize = new ArrayList<>();
//		Map<String, Integer> m = new HashMap<>();
//		m.put("A", 123);
//		serialize.add(m);
//		byte[] str = a.serialize(serialize);
//		Object deserialize = a.deserialize(str);
//		assertNotNull(deserialize);
//	}
//
//}
