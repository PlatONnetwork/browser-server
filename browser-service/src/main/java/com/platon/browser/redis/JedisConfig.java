package com.platon.browser.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class JedisConfig {

    private int connectionTimeout = 30000;
    private int soTimeout = 30000;
    private int maxRedirections = 30000;

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    // redis集群池配置
    private GenericObjectPoolConfig<Jedis> poolConfig;

    // 连接主机信息
    private Set<HostAndPort> jedisClusterNodes;

	public GenericObjectPoolConfig<Jedis> getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(GenericObjectPoolConfig<Jedis> poolConfig) {
        this.poolConfig = poolConfig;
    }

    public Set<HostAndPort> getJedisClusterNodes() {
        return jedisClusterNodes;
    }

    public void setJedisClusterNodes(Set<HostAndPort> jedisClusterNodes) {
        this.jedisClusterNodes = jedisClusterNodes;
    }

    @Override
    public String toString() {
        return "JedisConfig{" +
                "connectionTimeout=" + connectionTimeout +
                ", soTimeout=" + soTimeout +
                ", maxRedirections=" + maxRedirections +
                ", poolConfig=" + poolConfig +
                ", jedisClusterNodes=" + jedisClusterNodes +
                '}';
    }
}
