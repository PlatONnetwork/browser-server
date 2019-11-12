package com.platon.browser.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisCommands {

	String set(final String key, final String value);

    String set(final String key, final String value, final int expire);
    
    String setNX(final String key, final String value);
    
    String set(final byte[] key, final byte[] value);

    String set(final byte[] key, final byte[] value, final int expire);

    String get(final String key);
    
    String del(final String key);
    
    String expire(final String key, final int expire);
    
    byte[] get(final byte[] key);

    Long hset(final byte[] key, final byte[] field, final byte[] value);

    String hmset(final byte[] key, final Map<byte[], byte[]> hash);

    byte[] hget(final byte[] key, final byte[] field);

    List<byte[]> hmget(final byte[] key, final byte[]... fields);

    Long hdel(final byte[] key, final byte[]... field);

    Map<byte[], byte[]> hlistAll(final byte[] key);

    long rpush(final byte[] key, final byte[]... strings);

    long lpush(final byte[] key, final byte[]... strings);

    byte[] lpop(final byte[] key);
    
    Set<String> zrange(String key,Long start,Long end);
    
    Set<String> zrevrange(String key,Long start,Long end);
    
    long zsize(String key);
}
