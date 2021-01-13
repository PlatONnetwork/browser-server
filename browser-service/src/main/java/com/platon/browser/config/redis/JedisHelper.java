//package com.platon.browser.config.redis;
//
//import com.platon.browser.config.RedisClusterConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisCluster;
//import redis.clients.jedis.ShardedJedis;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * jedis对象创建辅助类
// *  @file JedisHelper.java
// *  @description
// *	@author zhangrj
// *  @data 2019年11月13日
// */
//@Slf4j
//@Component
//public class JedisHelper {
//	@Resource
//	private RedisClusterConfig redisClusterConfig;
//	private static Map<String, JedisCluster> redisMaps = new HashMap<>();
//
//	/**
//	 * 获取和设置redis类
//	 * @method getJedisClusterByKey
//	 * @param configKey
//	 * @return
//	 */
//	public JedisCluster getJedisClusterByKey(String configKey) {
//		JedisCluster jedisCluster = null;
//		if(StringUtils.isBlank(redisClusterConfig.getMasterIPs())) return jedisCluster;
//		JedisConfig jedisConfig = new JedisConfig();
//		jedisConfig.setConnectionTimeout(redisClusterConfig.getConnectionTimeout());
//		jedisConfig.setSoTimeout(redisClusterConfig.getSoTimeout());
//		jedisConfig.setMaxRedirections(redisClusterConfig.getMaxRedirections());
//		GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
//		poolConfig.setMaxTotal(redisClusterConfig.getMaxTotal());
//		poolConfig.setMaxIdle(redisClusterConfig.getMaxIdle());
//		poolConfig.setMinIdle(redisClusterConfig.getMinIdle());
//		poolConfig.setTestOnBorrow(redisClusterConfig.isTestOnBorrow());
//		poolConfig.setTestOnReturn(redisClusterConfig.isTestOnReturn());
//		poolConfig.setTestWhileIdle(redisClusterConfig.isTestWhileIdle());
//		Set<HostAndPort> jedisClusterNodes = new HashSet<>();
//		String[] ips = redisClusterConfig.getMasterIPs().split(",");
//		for(String ip: ips){
//			String[] arr = ip.split(":");
//			HostAndPort hostAndPort = new HostAndPort(arr[0], Integer.parseInt(arr[1]));
//			jedisClusterNodes.add(hostAndPort);
//		}
//		jedisConfig.setJedisClusterNodes(jedisClusterNodes);
//		jedisConfig.setPoolConfig(poolConfig);
//		if (!redisMaps.containsKey(configKey)) {
//			jedisCluster = getJedisCluster(jedisConfig);
//			redisMaps.put(configKey, jedisCluster);
//		} else {
//			jedisCluster = redisMaps.get(configKey);
//		}
//		return jedisCluster;
//	}
//
//	public JedisCluster getJedisCluster(JedisConfig jedisConfig){
//
//		JedisCluster jedisCluster;
//		if (jedisConfig.getJedisClusterNodes().isEmpty()) {
//			log.error("Redis [masterIPs] 参数未配置！");
//			return null;
//		}
//		try {
//			jedisCluster = new JedisCluster(jedisConfig.getJedisClusterNodes(),
//					jedisConfig.getConnectionTimeout(),
//					jedisConfig.getSoTimeout(),
//					jedisConfig.getMaxRedirections(),
//					redisClusterConfig.getPassword(),
//					jedisConfig.getPoolConfig());
//		} catch (Exception e) {
//			log.error("Redis 创建Redis连接池失败 ，请检查相关配置项！", e);
//			return null;
//		}
//		return jedisCluster;
//	}
//
//	public Boolean checkRedisConfig(String host, int port) {
//		if (null == host || "".equals(host.trim())) {
//			return false;
//		}
//		return port != 0;
//	}
//
//	/**
//	 * 关闭jedis
//	 * @method closeShardedJedis
//	 * @param jedisCluster
//	 * @param key
//	 */
//	public void closeShardedJedis(JedisCluster jedisCluster, String key) {
//		if (null != jedisCluster) {
//			try {
//				getJedisClusterByKey(key).close();
//			} catch (Exception e) {
//				log.error("",e);
//			}
//		}
//	}
//
//	public void recovery(ShardedJedis shardedJedis, boolean recovery) {
//		try {
//			if (shardedJedis != null && !recovery) {
//				shardedJedis.disconnect();
//			}
//			if (recovery && shardedJedis != null) {
//				shardedJedis.close();
//			}
//		} catch (Exception e) {
//			log.error("Redis ShardedJedis返回资源池完成!!!", e);
//		}
//	}
//}
