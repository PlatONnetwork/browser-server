//package com.platon.browser.config.redis;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * redis提供调用方法接口
// *
// * @file RedisCommands.java
// * @description
// * @author zhangrj
// * @data 2019年11月13日
// */
//public interface RedisCommands {
//
//    /**
//     * 设置string的key和value
//     *
//     * @method set
//     * @param key
//     * @param value
//     * @return
//     */
//    String set(final String key, final String value);
//
//    /**
//     * 设置string的key、value和expire
//     *
//     * @method set
//     * @param key
//     * @param value
//     * @param expire
//     * @return
//     */
//    String set(final String key, final String value, final int expire);
//
//    /**
//     * 设置string的key、value对应nx，用于分布式锁
//     *
//     * @method setNX
//     * @param key
//     * @param value
//     * @return
//     */
//    String setNX(final String key, final String value);
//
//    /**
//     * 设置byte的key、value
//     *
//     * @method set
//     * @param key
//     * @param value
//     * @return
//     */
//    String set(final byte[] key, final byte[] value);
//
//    /**
//     * 设置byte的key、value和expire
//     *
//     * @method set
//     * @param key
//     * @param value
//     * @param expire
//     * @return
//     */
//    String set(final byte[] key, final byte[] value, final int expire);
//
//    /**
//     * 根据string的key查询数据
//     *
//     * @method get
//     * @param key
//     * @return
//     */
//    String get(final String key);
//
//    /**
//     * 根据string的key删除数据
//     *
//     * @method del
//     * @param key
//     * @return
//     */
//    String del(final String key);
//
//    /**
//     * 根据string的key设置expire
//     *
//     * @method expire
//     * @param key
//     * @param expire
//     * @return
//     */
//    String expire(final String key, final int expire);
//
//    /**
//     * 根据byte的key查询数据
//     *
//     * @method get
//     * @param key
//     * @return
//     */
//    byte[] get(final byte[] key);
//
//    /**
//     * 根据byte的key、filed、value设置hash
//     *
//     * @method hset
//     * @param key
//     * @param field
//     * @param value
//     * @return
//     */
//    Long hset(final byte[] key, final byte[] field, final byte[] value);
//
//    /**
//     * 根据byte的key、map设置map
//     *
//     * @method hmset
//     * @param key
//     * @param hash
//     * @return
//     */
//    String hmset(final byte[] key, final Map<byte[], byte[]> hash);
//
//    /**
//     * 根据byte的key、field获取hash
//     *
//     * @method hget
//     * @param key
//     * @param field
//     * @param value
//     * @return
//     */
//    byte[] hget(final byte[] key, final byte[] field);
//
//    /**
//     * 根据byte的key、field获取list hash
//     *
//     * @method hmget
//     * @param key
//     * @param fields
//     * @return
//     */
//    List<byte[]> hmget(final byte[] key, final byte[]... fields);
//
//    /**
//     * 根据byte的key、field删除hash
//     *
//     * @method hdel
//     * @param key
//     * @param field
//     * @return
//     */
//    Long hdel(final byte[] key, final byte[]... field);
//
//    Map<byte[], byte[]> hlistAll(final byte[] key);
//
//    long rpush(final byte[] key, final byte[]... strings);
//
//    long lpush(final byte[] key, final byte[]... strings);
//
//    byte[] lpop(final byte[] key);
//
//    /**
//     * 根据key、start、end后去set正序range
//     *
//     * @method zrange
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    Set<String> zrange(String key, Long start, Long end);
//
//    /**
//     * 根据key、start、end后去set倒序range
//     *
//     * @method zrevrange
//     * @param key
//     * @param start
//     * @param end
//     * @return
//     */
//    Set<String> zrevrange(String key, Long start, Long end);
//
//    /**
//     * 获取set的长度
//     *
//     * @method zsize
//     * @param key
//     * @return
//     */
//    long zsize(String key);
//
//    String setnx(final String key, final String value, final long expire);
//}
