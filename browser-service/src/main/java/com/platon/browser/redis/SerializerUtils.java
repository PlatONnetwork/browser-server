package com.platon.browser.redis;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 序列化工具了
 *  @file SerializerUtils.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月13日
 */
@Slf4j
public class SerializerUtils {
    private SerializerUtils(){}

    private final static JedisSerializer<Object> redisSerializer = new JdkSerializer<>();


	public static JedisSerializer<Object> getRedisSerializer() {
        return redisSerializer;
    }

    //key 值 序列化
    public static byte[] rawKey(Object key) {
        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        return redisSerializer.serialize(key);
    }

    //value值序列化
    public static byte[] rawValue(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        return redisSerializer.serialize(value);
    }

    //序列化集合
    public static byte[][] rawValues(Object... values) {
        final byte[][] rawValues = new byte[values.length][];
        int i = 0;
        for (Object value : values) {
            rawValues[i++] = rawValue(value);
        }
        return rawValues;
    }


    //序列化集合
    @SuppressWarnings({"unchecked"})
    public static <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues, Class<T> type,
                                                                JedisSerializer<?> redisSerializer) {

        if (redisSerializer == null) {
            return (T) rawValues;
        }
        // connection in pipeline/multi mode
        if (rawValues == null) {
            return null;
        }

        Collection<Object> values = (List.class.isAssignableFrom(type) ? new ArrayList<Object>(rawValues.size())
                : new LinkedHashSet<Object>(rawValues.size()));
        for (byte[] bs : rawValues) {
            values.add(redisSerializer.deserialize(bs));
        }

        return (T) values;
    }

    public static <HK> byte[] rawHashKey(HK hashKey) {
        if (hashKey instanceof byte[]) {
            return (byte[]) hashKey;
        }
        return redisSerializer.serialize(hashKey);
    }

    public static <HV> byte[] rawHashValue(HV value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        return redisSerializer.serialize(value);
    }

    @SuppressWarnings("unchecked")
	public static <HK> byte[][] rawHashKeys(HK... hashKeys) {
        final byte[][] rawHashKeys = new byte[hashKeys.length][];
        int i = 0;
        for (HK hashKey : hashKeys) {
            rawHashKeys[i++] = rawHashKey(hashKey);
        }
        return rawHashKeys;
    }

    public static Object deserializeStr(byte[] byt) {
        return redisSerializer.deserialize(byt);
    }

    /**
     * 将Map对象反序列化
     *
     * @param byteMap
     * @return
     */
    public static Map<String, Object> deserializerMapObj(Map<byte[], byte[]> byteMap) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            for (Map.Entry<?, ?> tmpMap : byteMap.entrySet()) {
                byte[] byteKey = (byte[]) tmpMap.getKey();
                byte[] byteValue = (byte[]) tmpMap.getValue();
                result.put((String) deserializeStr(byteKey), deserializeStr(byteValue));
            }
            return result;
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }
}
