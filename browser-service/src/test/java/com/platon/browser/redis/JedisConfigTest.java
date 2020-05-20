package com.platon.browser.redis;

import org.junit.Test;

public class JedisConfigTest {

	@Test
	public void test() {
		JedisConfig jedisConfig = new JedisConfig();
		jedisConfig.getConnectionTimeout();
		jedisConfig.getSoTimeout();
		jedisConfig.getMaxRedirections();
		jedisConfig.setPoolConfig(null);
		jedisConfig.getPoolConfig();
		jedisConfig.setJedisClusterNodes(null);
		jedisConfig.getJedisClusterNodes();
		jedisConfig.toString();
	}
	
}
