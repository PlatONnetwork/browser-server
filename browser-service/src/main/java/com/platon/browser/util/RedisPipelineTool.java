package com.platon.browser.util;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/4/12 16:46
 * @Description:
 */
public class RedisPipelineTool {
    private RedisPipelineTool(){}
    /**
     * 通过多个键值批量查询值
     * @param keys
     * @param useParallel
     * @param clazz
     * @param redisTemplate
     * @param <T>
     * @return
     */
    public static  <T> Map<String,T> batchQueryByKeys(List<String> keys, Boolean useParallel, Class<T> clazz, RedisTemplate<String,String> redisTemplate){
        if(null == keys || keys.isEmpty() ) return null;
        if(null == useParallel) useParallel = true;

        List<Object> results = redisTemplate.executePipelined( (RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
            keys.forEach(stringRedisConn::get);
            return null;
        });
        if(null == results || results.isEmpty() ) return null;

        Map<String,T> resultMap;
        if(useParallel){
            Map<String,T> resultMapOne  = Collections.synchronizedMap(new HashMap<>());
            keys.parallelStream().forEach(key -> {
                String res = (String)results.get(keys.indexOf(key));
                resultMapOne.put(key, JSON.parseObject(res,clazz));
            });
            resultMap = resultMapOne;
        }else{
            Map<String,T> resultMapTwo  = new HashMap<>();
            keys.forEach(key-> {
                String res = (String)results.get(keys.indexOf(key));
                resultMapTwo.put(key,JSON.parseObject(res,clazz));
            });
            resultMap = resultMapTwo;
        }
        return  resultMap;
    }

    /**
     * 通过多个键值批量删除值
     */
    public static Integer batchDeleteByKeys(List<String> keys, Boolean useParallel, RedisTemplate<String,String> redisTemplate){
        if(null == keys || keys.isEmpty() ) return null;
        List<Object> results = redisTemplate.executePipelined( (RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
            keys.forEach(stringRedisConn::del);
            return null;
        });
        return results.size();
    }
}
