//package com.platon.browser.config.redis;
//
//import static org.junit.Assert.assertTrue;
//
//import com.platon.browser.config.redis.JedisSerializeUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class JedisSerializeUtilTest {
//
//	@Test
//	public void test() {
//		Object str = "tobytes";
//		byte[] serialize = JedisSerializeUtil.serialize(str);
//		Object deSeialize = JedisSerializeUtil.deSeialize(serialize);
//		assertTrue("tobytes".equals(deSeialize));
//	}
//
//}
