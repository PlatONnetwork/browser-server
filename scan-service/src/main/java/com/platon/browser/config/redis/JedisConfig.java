//package com.platon.browser.config.redis;
//
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.Jedis;
//
//import java.util.Set;
//
///**
// * jedis默认配置参数
// *  @file JedisConfig.java
// *  @description
// *	@author zhangrj
// *  @data 2019年11月13日
// */
//public class JedisConfig {
//
//	/**
//	 * 连接超时时间
//	 */
//    private int connectionTimeout = 30000;
//    /**
//     * socket通讯超时时间
//     */
//    private int soTimeout = 30000;
//    private int maxRedirections = 30000;
//
//    public int getConnectionTimeout() {
//        return connectionTimeout;
//    }
//
//    public void setConnectionTimeout(int connectionTimeout) {
//        this.connectionTimeout = connectionTimeout;
//    }
//
//    public int getSoTimeout() {
//        return soTimeout;
//    }
//
//    public void setSoTimeout(int soTimeout) {
//        this.soTimeout = soTimeout;
//    }
//
//    public int getMaxRedirections() {
//        return maxRedirections;
//    }
//
//    public void setMaxRedirections(int maxRedirections) {
//        this.maxRedirections = maxRedirections;
//    }
//
//    // redis集群池配置
//    private GenericObjectPoolConfig<Jedis> poolConfig;
//
//    // 连接主机信息
//    private Set<HostAndPort> jedisClusterNodes;
//
//	public GenericObjectPoolConfig<Jedis> getPoolConfig() {
//        return poolConfig;
//    }
//
//    public void setPoolConfig(GenericObjectPoolConfig<Jedis> poolConfig) {
//        this.poolConfig = poolConfig;
//    }
//
//    public Set<HostAndPort> getJedisClusterNodes() {
//        return jedisClusterNodes;
//    }
//
//    public void setJedisClusterNodes(Set<HostAndPort> jedisClusterNodes) {
//        this.jedisClusterNodes = jedisClusterNodes;
//    }
//
//    /**
//     * 重写tostring方法
//     */
//    @Override
//    public String toString() {
//        return "JedisConfig{" +
//                "connectionTimeout=" + connectionTimeout +
//                ", soTimeout=" + soTimeout +
//                ", maxRedirections=" + maxRedirections +
//                ", poolConfig=" + poolConfig +
//                ", jedisClusterNodes=" + jedisClusterNodes +
//                '}';
//    }
//}
