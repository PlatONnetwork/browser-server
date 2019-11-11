package com.platon.browser.redis;

import redis.clients.jedis.JedisCluster;


/**
 * JedisCallback 回调接口
 *
 */
public interface JedisCallback<T> {

	T doInRedis(JedisCluster jedisCluster) throws Exception;
}
