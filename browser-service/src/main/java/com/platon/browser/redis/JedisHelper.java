package com.platon.browser.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platon.browser.config.RedisConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * jedis对象创建辅助类
 *  @file JedisHelper.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月13日
 */
public class JedisHelper {

	private Logger logger = LoggerFactory.getLogger(JedisHelper.class);

	private static Map<String, JedisCluster> redisMaps = new HashMap<String, JedisCluster>();

	private JedisHelper() {
	}

	/**
	 * 获取和设置redis类
	 * @method getJedisClusterByKey
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	public JedisCluster getJedisClusterByKey(String configKey) throws Exception {
		JedisCluster jedisCluster = null;
		JedisConfig jedisConfig = new JedisConfig();
		jedisConfig.setConnectionTimeout(Integer
				.parseInt(RedisConfig.redisClusterConnectionTimeout));
		jedisConfig.setSoTimeout(Integer
				.parseInt(RedisConfig.redisClusterSoTimeout));
		jedisConfig.setMaxRedirections(Integer
				.parseInt(RedisConfig.redisClusterMaxRedirections));
		GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<Jedis>();
		poolConfig.setMaxTotal(Integer
				.parseInt(RedisConfig.redisClusterMaxTotal));
		poolConfig
				.setMaxIdle(Integer.parseInt(RedisConfig.redisClusterMaxIdle));
		poolConfig
				.setMinIdle(Integer.parseInt(RedisConfig.redisClusterMinIdle));
		poolConfig.setTestOnBorrow(Boolean
				.valueOf(RedisConfig.redisClusterTestOnBorrow));
		poolConfig.setTestOnReturn(Boolean
				.valueOf(RedisConfig.redisClusterTestOnReturn));
		poolConfig.setTestWhileIdle(Boolean
				.valueOf(RedisConfig.redisClusterTestWhileIdle));
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] ips = RedisConfig.redisClusterMasterIPs.split(",");
		for(String ip: ips){
			String[] arr = ip.split(":");
			HostAndPort hostAndPort = new HostAndPort(arr[0], Integer.parseInt(arr[1]));
			jedisClusterNodes.add(hostAndPort);
		}
		jedisConfig.setJedisClusterNodes(jedisClusterNodes);
		jedisConfig.setPoolConfig(poolConfig);
		if (!redisMaps.containsKey(configKey)) {
			jedisCluster = getJedisCluster(jedisConfig);
			redisMaps.put(configKey, jedisCluster);
		} else {
			jedisCluster = redisMaps.get(configKey);
		}
		return jedisCluster;
	}

	public JedisCluster getJedisCluster(JedisConfig jedisConfig)
			throws Exception {

		JedisCluster jedisCluster = null;
		if (jedisConfig.getJedisClusterNodes().size() <= 0) {
			logger.error("Redis [masterIPs] 参数未配置！");
			return null;
		}
		try {
			jedisCluster = new JedisCluster(jedisConfig.getJedisClusterNodes(),
					jedisConfig.getConnectionTimeout(),
					jedisConfig.getSoTimeout(),
					jedisConfig.getMaxRedirections(),
					RedisConfig.getRedisClusterPassword(),
					jedisConfig.getPoolConfig());
		} catch (Exception e) {
			logger.error("Redis 创建Redis连接池失败 ，请检查相关配置项！", e);
			return null;
		}
		return jedisCluster;
	}

	public Boolean checkRedisConfig(String host, int port) {
		if (null == host || "".equals(host.trim())) {
			return false;
		}
		if (null == (port + "") || port == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 关闭jedis
	 * @method closeShardedJedis
	 * @param jedisCluster
	 * @param key
	 */
	public void closeShardedJedis(JedisCluster jedisCluster, String key) {
		if (null != jedisCluster) {
			try {
				getJedisClusterByKey(key).close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void recovery(ShardedJedis shardedJedis, String key, boolean recovery) {
		try {
			if (shardedJedis != null && !recovery) {
				// getJedisPoolByKey(key).returnBrokenResource(shardedJedis);
				shardedJedis.disconnect();
			}
			if (recovery && shardedJedis != null) {
				shardedJedis.close();
				// getJedisPoolByKey(key).returnBrokenResource(shardedJedis);
			}
			;
		} catch (Exception e) {
			logger.error("Redis ShardedJedis返回资源池完成!!!", e);
		}
	}

	private static class RedisUtilHolder {
		private static JedisHelper instance = new JedisHelper();
	}

	/**
	 * 当getInstance方法第一次被调用的时候，它第一次读取
	 * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
	 * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
	 * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
	 */
	public static JedisHelper getInstance() {
		return RedisUtilHolder.instance;
	}

}
