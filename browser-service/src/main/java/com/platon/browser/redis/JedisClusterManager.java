package com.platon.browser.redis;


import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platon.browser.config.RedisConfig;

public class JedisClusterManager implements RedisCommands {

	private static Logger logger = LoggerFactory.getLogger(JedisClusterManager.class);

	private static JedisClusterManager jedisClusterManager;
	//private JedisExecute jedisExecute = null;
	private JedisCluster jedisCluster = null;
	private static String defaultConfigKey ;

	public static synchronized JedisClusterManager getInstance() {
		defaultConfigKey = RedisConfig.getRedisConfigKey();
		if (jedisClusterManager == null) {
			jedisClusterManager = new JedisClusterManager();
		}
		return jedisClusterManager;
	}

	private JedisClusterManager() {
		try {
			jedisCluster = JedisHelper.getInstance().getJedisClusterByKey(defaultConfigKey);
			//jedisExecute = new JedisExecute(jedisCluster);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String set(final byte[] key,final byte[] value) {
		String result = execute(new JedisCallback<String>() {
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.set(key,value);
			}
		});
		return result;
	}

	@Override
	public String set(final byte[] key, final byte[] value, final int expire) {
		String result = execute(new JedisCallback<String>() {
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.setex(key, expire, value);
			}
		});
		return result;
	}

	@Override
	public byte[] get(final byte[] key) {
		byte[] value = execute(new JedisCallback<byte[]>() {
			public byte[] doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.get(key);
			}
		});
		return value;
	}

	@Override
	public Long hset(final byte[] key, final byte[] field, final byte[] value){
		Long result = execute(new JedisCallback<Long>() {
			@Override
			public Long doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.hset(key, field, value);
			}
		});
		return result;
	}

	@Override
	public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.hmset(key, hash);
			}
		});
		return result;
	}

	@Override
	public byte[] hget(final byte[] key, final byte[] field) {
		byte[] result = execute(new JedisCallback<Object>() {
			@Override
			public Object doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.hget(key, field);
			}
		});
		return result;
	}

	@Override
	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		List<byte[]> result = execute(new JedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.hmget(key, fields);
			}
		});
		return result;
	}

	@Override
	public Long hdel(final byte[] key, final byte[]... field) {
		Long result = execute(new JedisCallback<Long>() {
			@Override
			public Long doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.hdel(key, field);
			}
		});
		return result;
	}

	@Override
	public Map<byte[], byte[]> hlistAll(final byte[] key) {
		Map<byte[], byte[]> result = execute(new JedisCallback<Map<byte[], byte[]>>() {
			@Override
			public Map<byte[], byte[]> doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.hgetAll(key);
			}
		});
		return result;
	}

	@Override
	public long rpush(byte[] key, byte[]... strings) {
		Long result = execute(new JedisCallback<Long>() {
			@Override
			public Long doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.rpush(key, strings);
			}
		});
		return result;
	}

	@Override
	public long lpush(byte[] key, byte[]... strings) {
		Long result = execute(new JedisCallback<Long>() {
			@Override
			public Long doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.lpush(key, strings);
			}
		});
		return result;
	}

	@Override
	public byte[] lpop(byte[] key) {
		byte[] result = execute(new JedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.lpop(key);
			}
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T execute(JedisCallback<?> callback) {
		try {
			return (T) callback.doInRedis(jedisCluster);
		} catch (Exception e) {
			logger.error("Redis Execute执行回调失败!!", e);
		} finally {
			// jedisCluster.close();
		}
		return null;
	}

	@Override
	public String set(String key, String value) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.set(key, value);
			}
		});
		return result;
	}

	@Override
	public String set(String key, String value, int expire) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.setex(key, expire, value);
			}
		});
		return result;
	}

	@Override
	public String get(String key) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.get(key);
			}
		});
		if(StringUtils.isBlank(result)) {
			result = "";
		}
		return result;
	}

	@Override
	public String del(String key) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				if(jedisCluster.del(key).intValue() == 0){
					return "fail";
				} 
				return "OK";
			}
		});
		return result;
	}

	@Override
	public String setNX(String key, String value) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				if(jedisCluster.setnx(key, value).intValue() == 0){
					return "fail";
				} 
				return "OK";
			}
		});
		return result;
	}

	@Override
	public String expire(String key, int expire) {
		String result = execute(new JedisCallback<String>() {
			@Override
			public String doInRedis(JedisCluster jedisCluster) throws Exception {
				if(jedisCluster.expire(key, expire).intValue() == 0){
					return "fail";
				} 
				return "OK";
			}
		});
		return result;
	}

	@Override
	public Set<String> zrange(String key, Long start, Long end) {
		Set<String> result = execute(new JedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.zrange(key, start, end);
			}
		});
		if(result == null) {
			result = new TreeSet<>(); 
		}
		return result;
	}

	@Override
	public long zsize(String key) {
		Long result = execute(new JedisCallback<Long>() {
			@Override
			public Long doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.zcard(key);
			}
		});
		if (result == null) {
			result = 0l;
		}
		return result;
	}

	@Override
	public Set<String> zrevrange(String key, Long start, Long end) {
		Set<String> result = execute(new JedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(JedisCluster jedisCluster) throws Exception {
				return jedisCluster.zrevrange(key, start, end);
			}
		});
		if(result == null) {
			result = new TreeSet<>(); 
		}
		return result;
	}



}
