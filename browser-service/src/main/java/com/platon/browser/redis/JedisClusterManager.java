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
			/**
			 * 加载默认key的jedismap
			 */
			jedisCluster = JedisHelper.getInstance().getJedisClusterByKey(defaultConfigKey);
		} catch (Exception e) {
			log.error("",e);
		}
	}

	@Override
	public String set(final byte[] key,final byte[] value) {
		String result = execute((JedisCallback<String>) jedisCluster -> jedisCluster.set(key,value));
		return result;
	}

	@Override
	public String set(final byte[] key, final byte[] value, final int expire) {
		String result = execute((JedisCallback<String>) jedisCluster -> jedisCluster.setex(key, expire, value));
		return result;
	}

	@Override
	public byte[] get(final byte[] key) {
		byte[] value = execute((JedisCallback<byte[]>) jedisCluster -> jedisCluster.get(key));
		return value;
	}

	@Override
	public Long hset(final byte[] key, final byte[] field, final byte[] value){
		Long result = execute((JedisCallback<Long>) jedisCluster -> jedisCluster.hset(key, field, value));
		return result;
	}

	@Override
	public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		String result = execute((JedisCallback<String>) jedisCluster -> jedisCluster.hmset(key, hash));
		return result;
	}

	@Override
	public byte[] hget(final byte[] key, final byte[] field) {
		byte[] result = execute(jedisCluster -> jedisCluster.hget(key, field));
		return result;
	}

	@Override
	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		List<byte[]> result = execute((JedisCallback<List<byte[]>>) jedisCluster -> jedisCluster.hmget(key, fields));
		return result;
	}

	@Override
	public Long hdel(final byte[] key, final byte[]... field) {
		Long result = execute((JedisCallback<Long>) jedisCluster -> jedisCluster.hdel(key, field));
		return result;
	}

	@Override
	public Map<byte[], byte[]> hlistAll(final byte[] key) {
		Map<byte[], byte[]> result = execute((JedisCallback<Map<byte[], byte[]>>) jedisCluster -> jedisCluster.hgetAll(key));
		return result;
	}

	@Override
	public long rpush(byte[] key, byte[]... strings) {
		Long result = execute((JedisCallback<Long>) jedisCluster -> jedisCluster.rpush(key, strings));
		return result;
	}

	@Override
	public long lpush(byte[] key, byte[]... strings) {
		Long result = execute((JedisCallback<Long>) jedisCluster -> jedisCluster.lpush(key, strings));
		return result;
	}

	@Override
	public byte[] lpop(byte[] key) {
		byte[] result = execute((JedisCallback<byte[]>) jedisCluster -> jedisCluster.lpop(key));
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T execute(JedisCallback<?> callback) {
		try {
			return (T) callback.doInRedis(jedisCluster);
		} catch (Exception e) {
			log.error("Redis Execute执行回调失败!!", e);
		} finally {
			// jedisCluster.close();
		}
		return null;
	}

	@Override
	public String set(String key, String value) {
		String result = execute((JedisCallback<String>) jedisCluster -> jedisCluster.set(key, value));
		return result;
	}

	@Override
	public String set(String key, String value, int expire) {
		String result = execute((JedisCallback<String>) jedisCluster -> jedisCluster.setex(key, expire, value));
		return result;
	}

	@Override
	public String get(String key) {
		String result = execute((JedisCallback<String>) jedisCluster -> jedisCluster.get(key));
		if(StringUtils.isBlank(result)) {
			result = "";
		}
		return result;
	}

	@Override
	public String del(String key) {
		String result = execute((JedisCallback<String>) jedisCluster -> {
			if(jedisCluster.del(key).intValue() == 0){
				return "fail";
			}
			return "OK";
		});
		return result;
	}

	@Override
	public String setNX(String key, String value) {
		String result = execute((JedisCallback<String>) jedisCluster -> {
			if(jedisCluster.setnx(key, value).intValue() == 0){
				return "fail";
			}
			return "OK";
		});
		return result;
	}

	@Override
	public String expire(String key, int expire) {
		String result = execute((JedisCallback<String>) jedisCluster -> {
			if(jedisCluster.expire(key, expire).intValue() == 0){
				return "fail";
			}
			return "OK";
		});
		return result;
	}

	@Override
	public Set<String> zrange(String key, Long start, Long end) {
		Set<String> result = execute((JedisCallback<Set<String>>) jedisCluster -> jedisCluster.zrange(key, start, end));
		if(result == null) {
			result = new TreeSet<>(); 
		}
		return result;
	}

	@Override
	public long zsize(String key) {
		Long result = execute((JedisCallback<Long>) jedisCluster -> jedisCluster.zcard(key));
		if (result == null) {
			result = 0l;
		}
		return result;
	}

	@Override
	public Set<String> zrevrange(String key, Long start, Long end) {
		Set<String> result = execute((JedisCallback<Set<String>>) jedisCluster -> jedisCluster.zrevrange(key, start, end));
		if(result == null) {
			result = new TreeSet<>(); 
		}
		return result;
	}
}
