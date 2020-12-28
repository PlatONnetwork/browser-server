package com.platon.browser.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class RedisConfig {
	private static String redisConfigKey;
	private static String redisHost;
	private static String redisPort;
	private static String redisModel;
	private static String redisExpire;
	private static String redisClusterConfigKey;
	private static String redisClusterConnectionTimeout;
	private static String redisClusterSoTimeout;
	private static String redisClusterMaxRedirections;
	private static String redisClusterMaxTotal;
	private static String redisClusterMaxIdle;
	private static String redisClusterMinIdle;
	private static String redisClusterMaxWaitMillis;
	private static String redisClusterTestOnBorrow;
	private static String redisClusterTestOnReturn;
	private static String redisClusterTestWhileIdle;
	private static String redisClusterMasterIPs;
	private static String redisClusterPassword;
	private static String redisClusterSSL;


    public static String getRedisConfigKey() {
        return redisConfigKey;
    }
	@Value("${redis.configKey:DEFAULT}")
	public void setRedisConfigKey(String redisConfigKey) {
		RedisConfig.redisConfigKey = redisConfigKey;
	}

	public static String getRedisHost() {
		return redisHost;
	}

	@Value("${redis.host:}")
	public void setRedisHost(String redisHost) {
		RedisConfig.redisHost = redisHost;
	}

	public String getRedisPort() {
		return redisPort;
	}

	@Value("${redis.port:}")
	public void setRedisPort(String redisPort) {
		RedisConfig.redisPort = redisPort;
	}

	public static String getRedisModel() {
		return redisModel;
	}

	@Value("${redis.model:}")
	public void setRedisModel(String redisModel) {
		RedisConfig.redisModel = redisModel;
	}

	public static String getRedisExpire() {
		return redisExpire;
	}

	@Value("${redis.expire:}")
	public void setRedisExpire(String redisExpire) {
		RedisConfig.redisExpire = redisExpire;
	}
	
	public static String getRedisClusterConfigKey() {
		return redisClusterConfigKey;
	}

	@Value("${redisCluster.configKey:}")
	public static void setRedisClusterConfigKey(String redisClusterConfigKey) {
		RedisConfig.redisClusterConfigKey = redisClusterConfigKey;
	}

	public static String getRedisClusterConnectionTimeout() {
		return redisClusterConnectionTimeout;
	}
	
	@Value("${redisCluster.connectionTimeout:}")
	public void setRedisClusterConnectionTimeout(
			String redisClusterConnectionTimeout) {
		RedisConfig.redisClusterConnectionTimeout = redisClusterConnectionTimeout;
	}

	public static String getRedisClusterSoTimeout() {
		return redisClusterSoTimeout;
	}

	@Value("${redisCluster.soTimeout:}")
	public void setRedisClusterSoTimeout(String redisClusterSoTimeout) {
		RedisConfig.redisClusterSoTimeout = redisClusterSoTimeout;
	}

	public static String getRedisClusterMaxRedirections() {
		return redisClusterMaxRedirections;
	}

	@Value("${redisCluster.maxRedirections:}")
	public void setRedisClusterMaxRedirections(
			String redisClusterMaxRedirections) {
		RedisConfig.redisClusterMaxRedirections = redisClusterMaxRedirections;
	}

	public static String getRedisClusterMaxTotal() {
		return redisClusterMaxTotal;
	}

	@Value("${redisCluster.maxTotal:}")
	public void setRedisClusterMaxTotal(String redisClusterMaxTotal) {
		RedisConfig.redisClusterMaxTotal = redisClusterMaxTotal;
	}

	public static String getRedisClusterMaxIdle() {
		return redisClusterMaxIdle;
	}

	@Value("${redisCluster.maxIdle:}")
	public void setRedisClusterMaxIdle(String redisClusterMaxIdle) {
		RedisConfig.redisClusterMaxIdle = redisClusterMaxIdle;
	}

	public static String getRedisClusterMinIdle() {
		return redisClusterMinIdle;
	}

	@Value("${redisCluster.minIdle:}")
	public void setRedisClusterMinIdle(String redisClusterMinIdle) {
		RedisConfig.redisClusterMinIdle = redisClusterMinIdle;
	}

	public static String getRedisClusterMaxWaitMillis() {
		return redisClusterMaxWaitMillis;
	}

	@Value("${redisCluster.maxWaitMillis:}")
	public void setRedisClusterMaxWaitMillis(String redisClusterMaxWaitMillis) {
		RedisConfig.redisClusterMaxWaitMillis = redisClusterMaxWaitMillis;
	}

	public static String getRedisClusterTestOnBorrow() {
		return redisClusterTestOnBorrow;
	}

	@Value("${redisCluster.testOnBorrow:}")
	public void setRedisClusterTestOnBorrow(String redisClusterTestOnBorrow) {
		RedisConfig.redisClusterTestOnBorrow = redisClusterTestOnBorrow;
	}

	public static String getRedisClusterTestOnReturn() {
		return redisClusterTestOnReturn;
	}

	@Value("${redisCluster.testOnReturn:}")
	public void setRedisClusterTestOnReturn(String redisClusterTestOnReturn) {
		RedisConfig.redisClusterTestOnReturn = redisClusterTestOnReturn;
	}

	public static String getRedisClusterTestWhileIdle() {
		return redisClusterTestWhileIdle;
	}

	@Value("${redisCluster.testWhileIdle:}")
	public void setRedisClusterTestWhileIdle(String redisClusterTestWhileIdle) {
		RedisConfig.redisClusterTestWhileIdle = redisClusterTestWhileIdle;
	}

	public static String getRedisClusterMasterIPs() {
		return redisClusterMasterIPs;
	}

	@Value("${redisCluster.masterIPs:}")
	public void setRedisClusterMasterIPs(String redisClusterMasterIPs) {
		RedisConfig.redisClusterMasterIPs = redisClusterMasterIPs;
	}
	
	public static String getRedisClusterPassword() {
		return redisClusterPassword;
	}

	@Value("${redisCluster.password:}")
	public void setRedisClusterPassword(String redisClusterPassword) {
		RedisConfig.redisClusterPassword = redisClusterPassword;
	}
	
	public static String getRedisClusterSSL() {
		return redisClusterSSL;
	}

	@Value("${redisCluster.ssl:false}")
	public void setRedisClusterSSL(String redisClusterSSL) {
		RedisConfig.redisClusterSSL = redisClusterSSL;
	}
	
}
