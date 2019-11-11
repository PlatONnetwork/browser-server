package com.platon.browser.redis;

import redis.clients.jedis.JedisCluster;

public abstract class JedisNoResultCall implements JedisCallback<Object>{

	public Object doInRedis(JedisCluster jedisCluster) throws Exception {
		action(jedisCluster);
		return null;
	}
	
	public abstract void action(JedisCluster jedisCluster);

}
