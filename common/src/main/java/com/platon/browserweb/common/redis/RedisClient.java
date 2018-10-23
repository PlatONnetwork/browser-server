package com.platon.browserweb.common.redis;

import com.platon.browserweb.common.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisClient extends RedisBaseDao {
    private static Logger log = LoggerFactory.getLogger(RedisClient.class);
    private int expire;
    private JedisCluster jedisCluster;
    public static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 秒
     */
    public static final String SET_WITH_EXPIRE_TIME = "EX";

    /**
     * 设置key生命周期
     * @param prefix
     * @param key
     * @param timeout
     */
    public void expire(String prefix,String key,int timeout){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
                jedisCluster.expire(key.getBytes(),timeout);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("expire[expire操作]异常信息:{}", e.getMessage());
        }
    }

    /**
     * bytes类型set操作
     * @param prefix
     * @param key
     * @param bs
     */
    public void setBytes(String prefix,String key,byte [] bs){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)&& bs != null){
                jedisCluster.set(key.getBytes(),bs);
                jedisCluster.expire(key.getBytes(),expire);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("setBytes[set操作]异常信息:{}", e.getMessage());
        }
    }

    /**
     * bytes类型get操作
     * @param prefix
     * @param key
     */
    public byte [] getBytes(String prefix,String key){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
               return jedisCluster.get(key.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("getBytes[get操作]异常信息:{}", e.getMessage());
        }
        return null;
    }

    /**
     * object类型set操作
     * @param prefix
     * @param key
     * @param obj
     */
    public <T> void setObj(String prefix,String key,T obj){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)&& obj != null){
                byte [] value = JSONUtil.bean2Json(obj).getBytes();
                jedisCluster.set(key.getBytes(),value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("setObj[set操作]异常信息:{}", e.getMessage());
        }
    }

    /**
     * object类型get操作
     * @param prefix
     * @param key
     * @param cls
     * @return
     */
    public <T> T getObj(String prefix,String key,Class<T> cls){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
                byte [] value = jedisCluster.get(key.getBytes());
                if (value != null){
                    return JSONUtil.json2Bean(value.toString(), cls);
                }
            }
        } catch (Exception e) {
            log.info("getObj[get操作]异常信息:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取序列值
     * @param prefix
     * @param key
     * @return
     */
    public long getSequence(String prefix,String key){
        key = getKey(prefix, key);
        try {
            return jedisCluster.incr(key);
        }  catch (Exception e) {
            log.info("getSequence异常信息:{}", e.getMessage());
        }
        return 0;
    }


    /**
     * String类型set操作
     *
     * @param prefix 前缀
     * @param key    key值
     * @param value  值
     * @param expire 生命周期s
     */
    public void setString(String prefix, String key, String value,int expire) {
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)&& StringUtils.isNotEmpty(value)){
                jedisCluster.set(key, value);
                if (expire!=-1){
                    jedisCluster.expire(key,expire);
                }
            }
        } catch (Exception e) {
            log.info("redis[set操作]异常信息:{}", e.getMessage());
        }
    }

    /**
     * String类型get操作
     *
     * @param prefix 前缀
     * @param key    key值
     * @return
     */
    public String getString(String prefix, String key) {
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
                return jedisCluster.get(key);
            }
        } catch (Exception e) {
            log.info("redis[get操作]异常信息:{}", e.getMessage());
        }
        return null;
    }

    /**
     * hash类型set操作
     *
     * @param prefix 前缀
     * @param key    key值
     * @return
     */
    public void setHash(String prefix, String key ,String field ,String value){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
                jedisCluster.hset(key,field,value);
            }
        } catch (Exception e) {
            log.info("redis[hset操作]异常信息:{}", e.getMessage());
            throw e;
        }
    }

    /**
     * hash类型get操作
     *
     * @param prefix 前缀
     * @param key    key值
     * @return
     */
    public String getHash(String prefix, String key ,String field){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
               return jedisCluster.hget(key,field);
            }
        } catch (Exception e) {
            log.info("redis[hget操作]异常信息:{}", e.getMessage());
            throw e;
        }
        return null;
    }

    /**
     * hash类型getall操作
     *
     * @param prefix 前缀
     * @param key    key值
     * @return
     */
    public Map<String,String> getHashAll(String prefix, String key){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
               return jedisCluster.hgetAll(key);
            }
        }catch (Exception e) {
            log.info("redis[hgetall操作]异常信息:{}", e.getMessage());
        }
        return null;
    }
    
    /**
     * hash类型del操作
     *
     * @param prefix 前缀
     * @param key    key值
     * @return
     */
    public void hdel(String prefix , String key, final String... field) {
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
                jedisCluster.hdel(key, field);
            }
        }catch (Exception e) {
            log.info("redis[del操作]异常信息:{}", e.getMessage());
            throw e;
        }
    }

    public void del(String prefix, String key){
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)){
                jedisCluster.del(key);
            }
        }catch (Exception e) {
            log.info("redis[del操作]异常信息:{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 批量插入有序集合
     *
     * @param prefix 前缀
     * @param key    key值
     * @param map    数据
     */
    public void zadd(String prefix, String key, Map<String, Double> map) {
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)&& map != null){
                jedisCluster.zadd(key, map);
            }
        } catch (Exception e) {
            log.info("redis[zadd操作]异常信息:{}", e.getMessage());
        }
    }

    /**
     * 获取有序集合成员值
     *
     * @param prefix 前缀
     * @param key    key值
     * @param member 成员
     * @return
     */
    public String zscore(String prefix, String key, String member) {
        key = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(key)&& StringUtils.isNotEmpty(member)){
                BigDecimal bd = new BigDecimal(jedisCluster.zscore(key, member));
                bd.setScale(2,BigDecimal.ROUND_HALF_UP);
                return bd.toPlainString();
            }
        } catch (Exception e) {
            log.info("redis[zscore操作]异常信息:{}", e.getMessage());
        }
        return null;
    }

    public long increaseByLong(String db, String key, long value) {
        String keys = db + ":" + key;
        return jedisCluster.incrBy(keys, value);
    }

    public List<String> lrange(String prefix, String key, long start, long stop) {
        key = getKey(prefix, key);
        return jedisCluster.lrange(key, start, stop);
    }

    public void rpush(String prefix, String key, String... data) {
        key = getKey(prefix, key);
        jedisCluster.rpush(key, data);
    }

    public void zadd(String prefix, String key, double score, String member) {
        key = getKey(prefix, key);
        jedisCluster.zadd(key, score, member);
    }

    public Set<Tuple> zrangeByScoreWithScores(String prefix, String key, double min, double max) {
        key = getKey(prefix, key);
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }
    
    public boolean tryGetDistributedLock(String db, String lockKey, String requestId) {
        boolean flag = false;
        try {
        	flag = tryGetDistributedLock(db, lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 2L);
        } catch (Exception e) {
        	log.info("redis[分布式锁操作]异常信息:{}", e.getMessage());
        }

        return flag;
    }

    public boolean tryGetDistributedLock(String db, String lockKey, String requestId, String nx, String ex, long expireTime) {

    	String result = jedisCluster.set(getKey(db, lockKey), requestId, nx, ex, expireTime);

    	if("OK".equals(result)) {
    		return true;
    	}else {
    		return false;
    	}

    }

    public void releaseDistributedLock(String db, String lockKey, String requestId) {
        String key = getKey(db, lockKey);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        try {
        	eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
        } catch (Exception e) {
        	log.info("redis[释放分布式锁]异常信息:{}", e.getMessage());
        }

    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public RedisClient(int expire) {
        this.expire = expire;
    }


    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public void eval(String script, List<String> keys, List<String> args) {
        evalsha(script, keys, args);

    }

    public void evalsha(String script, List<String> keys, List<String> args) {
        try {
            jedisCluster.evalsha(DigestUtils.sha1DigestAsHex(script), keys, args);
        } catch (Throwable e) {
            if (ScriptUtils.exceptionContainsNoScriptError(e)) {
                // redis不存在lua脚本
                jedisCluster.eval(script, keys, args);
            }
            throw e;
        }
    }
}
