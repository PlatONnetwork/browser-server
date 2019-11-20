package com.platon.browser.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JedisHelper {

	private Logger logger = LoggerFactory.getLogger(JedisHelper.class);

	private static Map<String, JedisCluster> redisMaps = new HashMap<>();

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
				.parseInt(RedisConfig.getRedisClusterConnectionTimeout()));
		jedisConfig.setSoTimeout(Integer
				.parseInt(RedisConfig.getRedisClusterSoTimeout()));
		jedisConfig.setMaxRedirections(Integer
				.parseInt(RedisConfig.getRedisClusterMaxRedirections()));
		GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<Jedis>();
		poolConfig.setMaxTotal(Integer
				.parseInt(RedisConfig.getRedisClusterMaxTotal()));
		poolConfig
				.setMaxIdle(Integer.parseInt(RedisConfig.getRedisClusterMaxIdle()));
		poolConfig
				.setMinIdle(Integer.parseInt(RedisConfig.getRedisClusterMinIdle()));
		poolConfig.setTestOnBorrow(Boolean
				.valueOf(RedisConfig.getRedisClusterTestOnBorrow()));
		poolConfig.setTestOnReturn(Boolean
				.valueOf(RedisConfig.getRedisClusterTestOnReturn()));
		poolConfig.setTestWhileIdle(Boolean
				.valueOf(RedisConfig.getRedisClusterTestWhileIdle()));
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String[] ips = RedisConfig.getRedisClusterMasterIPs().split(",");
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
		if (jedisConfig.getJedisClusterNodes().isEmpty()) {
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
				log.error("",e);
			}
		}
	}

	public void recovery(ShardedJedis shardedJedis, String key, boolean recovery) {
		try {
			if (shardedJedis != null && !recovery) {
				shardedJedis.disconnect();
			}
			if (recovery && shardedJedis != null) {
				shardedJedis.close();
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
