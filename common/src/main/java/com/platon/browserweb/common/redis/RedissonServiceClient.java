/*
 * Copyright (c) 2018. juzix.io. All rights reserved.
 */

package com.platon.browserweb.common.redis;

import com.platon.browserweb.common.util.PropertyConfigurer;
import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RScript;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: CK
 * @date: 2018/8/30
 */
public class RedissonServiceClient {

    private static volatile RedissonClient redisson;

    // 提供redis默认本地缓存策略（一秒更新一次本地缓存）
    private static LocalCachedMapOptions localCachedMapOptions = LocalCachedMapOptions.defaults()
            .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.SOFT)
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
            .timeToLive(600, TimeUnit.SECONDS);

    private final static Lock lock = new ReentrantLock();

    public static RedissonClient getRedissonClient() {
        if (null == redisson) {
            try {
                lock.lock();

                //创建配置
                Config config = new Config();

                String nodeAddress = PropertyConfigurer.getStringValue("redis.ipport");
                String[] nodes = nodeAddress.split(",");
                for (int i = 0; i < nodes.length; i++) {
                    nodes[i] = "redis://" + nodes[i].trim();
                }
                //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
                //之前使用的spring-data-redis，用的客户端jedis，编码为org.springframework.data.redis.serializer.StringRedisSerializer
                //改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
                config.setCodec(new org.redisson.client.codec.StringCodec())
                        .useClusterServers()
                        .addNodeAddress(nodes)
                        .setMasterConnectionPoolSize(500) //设置对于master节点的连接池中连接数最大为500
                        .setSlaveConnectionPoolSize(500) //设置对于slave节点的连接池中连接数最大为500
                        .setIdleConnectionTimeout(10000) //如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
                        .setConnectTimeout(30000) //同任何节点建立连接时的等待超时。时间单位是毫秒。
                        .setTimeout(3000)//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
                        .setPingTimeout(30000)
                        .setFailedSlaveReconnectionInterval(3000);//当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。
                redisson = Redisson.create(config);
            } finally {
                lock.unlock();
            }
        }
        return redisson;
    }

    public static String getKey(String db, String key) {
        return db + ":" + key;
    }

    public static <V> RScoredSortedSet<V> getScoredSortedSet(String db, String key) {
        return getRedissonClient().getScoredSortedSet(getKey(db, key));
    }

    public static <K, V> RMap<K, V> getMap(String db, String key) {
        return getRedissonClient().getMap(getKey(db, key));
    }

    public static <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String db, String key) {
        return getRedissonClient().getLocalCachedMap(getKey(db, key), localCachedMapOptions);
    }

    public static <V> RBucket<V> getKV(String db, String key) {
        return getRedissonClient().getBucket(getKey(db, key));
    }

    public static RAtomicLong getAtomicLong(String db, String key) {
        return getRedissonClient().getAtomicLong(getKey(db, key));
    }

    public static <V> RList<V> getList(String db, String key) {
        return getRedissonClient().getList(getKey(db, key));
    }

    public static <V> RSet<V> getSet(String db, String key) {
        return getRedissonClient().getSet(getKey(db, key));
    }

    public static void eval(String script, List<Object> keys, Object... args) {
        evalsha(script, RScript.ReturnType.BOOLEAN, keys, args);
    }

    public static  <T> T eval(String script, RScript.ReturnType returnType, List<Object> keys, Object... args) {
        return evalsha(script, returnType, keys, args);
    }

    public static <T> T evalsha(String script, RScript.ReturnType returnType, List<Object> keys, Object... args) {
        try {
            String sha1 = DigestUtils.sha1DigestAsHex(script);
            return getRedissonClient().getScript().evalSha(RScript.Mode.READ_WRITE, sha1, returnType, keys, args);
        } catch (Throwable e) {
            if (ScriptUtils.exceptionContainsNoScriptError(e)) {
                // redis不存在lua脚本
                return getRedissonClient().getScript().eval(RScript.Mode.READ_WRITE, script, returnType, keys, args);
            }
            throw e;
        }
    }

    /**
     * 获取分布式锁（阻塞模式）默认过期时间2秒，等待获取锁时间30毫秒
     * @param db db
     * @param lockKey key
     * @param lockCall 业务逻辑
     */
    public static void tryFairLock(String db, String lockKey, LockCall lockCall) throws Throwable {
        // 默认线程睡30毫秒，过期2秒
        tryFairLock(db, lockKey, 2000L, 30L, lockCall);
    }

    /**
     * 获取分布式锁（阻塞模式）
     * @param db db
     * @param key key
     * @param expireTime 锁过期时间（毫秒）
     * @param waitTime 等待获取锁时间（毫秒）
     * @param lockCall 业务逻辑
     */
    public static void tryFairLock(String db, String key, long expireTime, long waitTime, LockCall lockCall) throws Throwable {
        // 尝试加锁
        String keys = db + ":" + key;
        RLock rLock = getRedissonClient().getFairLock(keys);
        try {
            boolean flag;
            do {
                // 获取不到锁，阻塞
                flag = rLock.tryLock(waitTime, expireTime, TimeUnit.MILLISECONDS);
            } while (!flag);
            lockCall.accept();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
    }

    /**
     * 获取分布式锁（阻塞模式）默认过期时间2秒，等待获取锁时间30毫秒
     * @param db db
     * @param lockKey key
     * @param lockCall 业务逻辑
     * @return boolean
     */
    public static boolean tryFairLockFast(String db, String lockKey, LockCall lockCall) throws Throwable {
        // 默认线程睡30毫秒，过期2秒
        return tryFairLockFast(db, lockKey, 2000L, 30L, lockCall);
    }

    /**
     * 获取分布式锁（非阻塞模式）
     * @param db db
     * @param key key
     * @param expireTime 锁过期时间（毫秒）
     * @param waitTime 等待获取锁时间（毫秒）
     * @param lockCall 业务逻辑
     * @return boolean
     */
    public static boolean tryFairLockFast(String db, String key, long expireTime, long waitTime, LockCall lockCall) throws Throwable {
        String keys = db + ":" + key;
        RLock rLock = getRedissonClient().getFairLock(keys);
        boolean flag = false;
        try{
            flag = rLock.tryLock(waitTime, expireTime, TimeUnit.MILLISECONDS);
            if (flag) lockCall.accept();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 非阻塞模式下自动释放锁
            rLock.unlock();
        }
        return flag;
    }

    /**
     * 业务逻辑回调
     */
    public interface LockCall {
        void accept() throws Throwable;
    }
}
