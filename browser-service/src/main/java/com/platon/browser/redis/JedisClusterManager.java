package com.platon.browser.redis;


import com.platon.browser.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 具体实现整个接口调用
 *  @file JedisClusterManager.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月13日
 */
@Slf4j
public class JedisClusterManager implements RedisCommands {
	private static JedisClusterManager jedisClusterManager;
	private JedisCluster jedisCluster = null;
	private static String defaultConfigKey ;

	/**
	 * 线程安全的初始化整个对象
	 * @method getInstance
	 * @return
	 */
	public static synchronized JedisClusterManager getInstance() {
		defaultConfigKey = RedisConfig.getRedisConfigKey();
		if (jedisClusterManager == null) {
			jedisClusterManager = new JedisClusterManager();
		}
		return jedisClusterManager;
	}

	private JedisClusterManager() {
		try {
			/*
			 * 加载默认key的jedismap
			 */
			jedisCluster = JedisHelper.getInstance().getJedisClusterByKey(defaultConfigKey);
		} catch (Exception e) {
			log.error("",e);
		}
	}

	@Override
	public String set(final byte[] key,final byte[] value) {
		return execute((JedisCallback<String>) cluster -> cluster.set(key,value));
	}

	@Override
	public String set(final byte[] key, final byte[] value, final int expire) {
		return execute((JedisCallback<String>) cluster -> cluster.setex(key, expire, value));
	}

	@Override
	public byte[] get(final byte[] key) {
		return execute((JedisCallback<byte[]>) cluster -> cluster.get(key));
	}

	@Override
	public Long hset(final byte[] key, final byte[] field, final byte[] value){
		return execute((JedisCallback<Long>) cluster -> cluster.hset(key, field, value));
	}

	@Override
	public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		return execute((JedisCallback<String>) cluster -> cluster.hmset(key, hash));
	}

	@Override
	public byte[] hget(final byte[] key, final byte[] field) {
		return execute(cluster -> cluster.hget(key, field));
	}

	@Override
	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		return execute((JedisCallback<List<byte[]>>) cluster -> cluster.hmget(key, fields));
	}

	@Override
	public Long hdel(final byte[] key, final byte[]... field) {
		return execute((JedisCallback<Long>) cluster -> cluster.hdel(key, field));
	}

	@Override
	public Map<byte[], byte[]> hlistAll(final byte[] key) {
		return execute((JedisCallback<Map<byte[], byte[]>>) cluster -> cluster.hgetAll(key));
	}

	@Override
	public long rpush(byte[] key, byte[]... strings) {
		return execute((JedisCallback<Long>) cluster -> cluster.rpush(key, strings));
	}

	@Override
	public long lpush(byte[] key, byte[]... strings) {
		return execute((JedisCallback<Long>) cluster -> cluster.lpush(key, strings));
	}

	@Override
	public byte[] lpop(byte[] key) {
		return execute((JedisCallback<byte[]>) cluster -> cluster.lpop(key));
	}

	@SuppressWarnings("unchecked")
	public <T> T execute(JedisCallback<?> callback) {
		try {
			return (T) callback.doInRedis(jedisCluster);
		} catch (Exception e) {
			log.error("Redis Execute执行回调失败!!", e);
		}
		return null;
	}

	@Override
	public String set(String key, String value) {
		return execute((JedisCallback<String>) cluster -> cluster.set(key, value));
	}

	@Override
	public String set(String key, String value, int expire) {
		return execute((JedisCallback<String>) cluster -> cluster.setex(key, expire, value));
	}

	@Override
	public String get(String key) {
		String result = execute((JedisCallback<String>) cluster -> cluster.get(key));
		if(StringUtils.isBlank(result)) {
			result = "";
		}
		return result;
	}

	@Override
	public String del(String key) {
		return execute((JedisCallback<String>) cluster -> {
			if(cluster.del(key).intValue() == 0){
				return "fail";
			}
			return "OK";
		});
	}

	@Override
	public String setNX(String key, String value) {
		return execute((JedisCallback<String>) cluster -> {
			if(cluster.setnx(key, value).intValue() == 0){
				return "fail";
			}
			return "OK";
		});
	}

	@Override
	public String expire(String key, int expire) {
		return execute((JedisCallback<String>) cluster -> {
			if(cluster.expire(key, expire).intValue() == 0){
				return "fail";
			}
			return "OK";
		});
	}

	@Override
	public Set<String> zrange(String key, Long start, Long end) {
		Set<String> result = execute((JedisCallback<Set<String>>) cluster -> cluster.zrange(key, start, end));
		if(result == null) {
			result = new TreeSet<>(); 
		}
		return result;
	}

	@Override
	public long zsize(String key) {
		Long result = execute((JedisCallback<Long>) cluster -> cluster.zcard(key));
		if (result == null) {
			result = 0L;
		}
		return result;
	}

	@Override
	public Set<String> zrevrange(String key, Long start, Long end) {
		Set<String> result = execute((JedisCallback<Set<String>>) cluster -> cluster.zrevrange(key, start, end));
		if(result == null) {
			result = new TreeSet<>(); 
		}
		return result;
	}
}
