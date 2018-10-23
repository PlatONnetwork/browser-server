package com.platon.browserweb.common.redis;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.JedisCommands;


public class RedisBaseDao {

    protected String getKey(String prefix, String key) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(prefix)) {
            sb.append(prefix).append(":");
        }
        sb.append(key);
        return sb.toString();
    }

    protected byte[] getKey(String prefix, byte[] key) {
        if (StringUtils.isNotEmpty(prefix)) {
            return ArrayUtils.addAll(prefix.getBytes(), key);
        } else {
            return key;
        }
    }

    protected String[] getBatchKey(String prefix, String... keys) {
        if (StringUtils.isEmpty(prefix)) {
            return keys;
        } else if (keys != null && keys.length > 0) {
            String[] keys_ = new String[keys.length];
            for (int i = 0; i < keys.length; i++) {
                keys_[i] = getKey(prefix, keys[i]);
            }
            return keys_;
        }
        return null;
    }

    protected byte[][] getBatchByteKey(String prefix, byte[]... keys) {
        if (StringUtils.isEmpty(prefix)) {
            return keys;
        } else if (keys != null && keys.length > 0) {
            byte[][] keys_ = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                keys_[i] = getKey(prefix, keys[i]);
            }
            return keys_;
        }
        return null;
    }

    /**
     * 设置数据生命周期(单位/s)
     *
     * @param jedis
     * @param key
     * @param expire
     */
    protected void jedisExpire(JedisCommands jedis, String key, Long expire) {
        if (expire != null && expire.intValue() != -1) {
            jedis.expire(key, expire.intValue());
        }
    }
}
