package com.platon.browser.redis;

import java.util.*;

public class SerializerUtils {


    private final static JedisSerializer<Object> redisSerializer = new JdkSerializer<Object>();


	public static JedisSerializer<Object> getRedisserializer() {
        return redisSerializer;
    }

    //key 值 序列化
    public static byte[] rawKey(Object key) {
        if (redisSerializer == null && key instanceof byte[]) {
            return (byte[]) key;
        }
        return redisSerializer.serialize(key);
    }

    //value值序列化
    public static byte[] rawValue(Object value) {
        if (redisSerializer == null && value instanceof byte[]) {
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
        if (redisSerializer == null && hashKey instanceof byte[]) {
            return (byte[]) hashKey;
        }
        return redisSerializer.serialize(hashKey);
    }

    public static <HV> byte[] rawHashValue(HV value) {
        if (redisSerializer == null & value instanceof byte[]) {
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
     * 序列化Map对象
     *
     * @param paramsMap Map对象
     * @return 返回序列化后的Map对象
     */
   /* public static Map<byte[], byte[]> serializerMapObj(Map<String, Object> paramsMap) {
        try {
            final Map<byte[], byte[]> hashes = new LinkedHashMap<byte[], byte[]>(paramsMap.size());
            for (Map.Entry<?, ?> entry : paramsMap.entrySet()) {
                Object valueObj = entry.getValue();
                // 判断valueObje 的类型：NativeObject->Map NativeArray->List ConsString->String
                byte[] byt = null;
                if (valueObj instanceof String) {
                    byt = SerializerUtils.rawValue(valueObj);
                } else if (valueObj instanceof NativeObject) {
                    Map<String, Object> tmpMap = (Map) valueObj;
                    byt = SerializerUtils.rawValue(JSONUtil.naviveObj2Map(tmpMap));
                } else if (valueObj instanceof NativeArray) {
                    List tmpList = (List) valueObj;
                    List resultList = new ArrayList();
                    // 特殊处理 [{},{},{}] 这种数据结构
                    for (int i = 0; i < tmpList.size(); i++) {
                        if (tmpList.get(i) instanceof NativeObject) {
                            Map m = (Map) tmpList.get(i);
                            resultList.add(JSONUtil.naviveObj2Map(m));
                        } else {
                            resultList.add(tmpList.get(i));
                        }
                    }
                    byt = SerializerUtils.rawValue(resultList);
                } else { // 普通javaMap对象
                    byt = SerializerUtils.rawValue(valueObj);
                }
                hashes.put(SerializerUtils.rawHashKey(entry.getKey() + ""), byt);
            }
            return hashes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

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
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 比较两个Map剔除重复项
     *
     * @param paramMap
     * @param allMap
     * @return
     */
    /*public static Map<byte[], byte[]> unquie2Map(Map<byte[], byte[]> paramMap, Map<byte[], byte[]> allMap) {
        Map<String, Object> paramMapStr = SerializerUtils.deserializerMapObj(paramMap);
        Map<String, Object> allMapStr = SerializerUtils.deserializerMapObj(allMap);
        Set<String> paramKeys = paramMapStr.keySet();
        Set<String> allKeys = allMapStr.keySet();
        for (String tmp : paramKeys) {
            if (allMapStr.containsKey(tmp)) {
                allMapStr.remove(tmp);
            }
        }
        for (Map.Entry<String, Object> allMapTmp : allMapStr.entrySet()) {
            // 合并
            paramMapStr.put(allMapTmp.getKey(), allMapTmp.getValue());
        }

        return SerializerUtils.serializerMapObj(paramMapStr);
    }*/


}
