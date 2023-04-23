package com.platon.browser.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.RedisKeyConfig;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: Redis服务
 */
@Slf4j
public abstract class AbstractRedisService<T> {
    @Resource
    protected RedisKeyConfig redisKeyConfig;
    @Resource
    protected RedisTemplate<String,String> redisTemplate;
    MinMaxScore minMax= MinMaxScore.builder().build();
    // 待入库元组列表
    Set<ZSetOperations.TypedTuple<String>> stageSet = new HashSet<>();
    // 本次操作的参数列表中已经在redis中存在的记录score
    Set<Long> existScore = new HashSet<>();

    @Data
    @Builder
    @Accessors(chain = true)
    public static class MinMaxScore{
        private Long minOffset;
        private Long maxOffset;
        MinMaxScore reset(){
            minOffset=Long.MAX_VALUE;
            maxOffset=Long.MIN_VALUE;
            return this;
        }
    }

    /**
     * 清除区块缓存
     */
    public void clear() {
        redisTemplate.delete(getCacheKey());
    }
    public abstract String getCacheKey();
    public void updateMinMaxScore(Set<T> data){}
    public void updateExistScore(Set<String> exist){}
    public void updateStageSet(Set<T> data){}

    /**
     * 模板方法，通用流程在此操作,具体属性由子类处理
     * @param data 需要入库到redis的数据集
     * @param serialOverride 需要逐条执行串行覆盖 例如统计记录的更新, 默认是批处理方式
     */
    public void save(Set<T> data, boolean serialOverride) {
        if(data.isEmpty()) return;
        long startTime = System.currentTimeMillis();

        if(serialOverride) {
            data.forEach(item -> {
                String json = JSON.toJSONString(item);
                redisTemplate.opsForValue().set(getCacheKey(), json);
            });
        }else{
            // 取出缓存中的记录总数
            Long cacheItemCount = redisTemplate.opsForZSet().size(getCacheKey());
            // 更新MinMax最大分数和最小分数
            updateMinMaxScore(data);
            // 查询在缓存中是否有值
            existScore.clear();
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(getCacheKey(),minMax.getMinOffset(),minMax.getMaxOffset());
            updateExistScore(exist);
            // 先清空待入库列表
            stageSet.clear();
            // 更新待入库列表
            updateStageSet(data);
            // 执行入库操作
            if(!stageSet.isEmpty()) redisTemplate.opsForZSet().add(getCacheKey(), stageSet);
            if(cacheItemCount!=null&&cacheItemCount>redisKeyConfig.getMaxItem()){
                // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (cacheItemCount-maxItemCount)个
                redisTemplate.opsForZSet().removeRange(getCacheKey(),0,cacheItemCount-redisKeyConfig.getMaxItem());
            }
        }
        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }

    public Long size(String key){
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey){
            return redisTemplate.opsForZSet().size(key);
        }
        return 0L;
    }
}
