package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.service.TransactionCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 交易缓存数据处理逻辑
 *  @file TransactionCacheServiceImpl.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Component
public class TransactionCacheServiceImpl implements TransactionCacheService {
    private final Logger logger = LoggerFactory.getLogger(TransactionCacheServiceImpl.class);
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /** 交易缓存key */
    @Value("${platon.redis.key.transactions}")
    private String transactionsCacheKey;
    @Value("${platon.redis.max-item}")
    private long maxItemCount;

    @Autowired
    private TransactionMapper transactionMapper;

    /**
     * 清除首页统计缓存
     */
    @Override
    public void clear() {
        redisTemplate.delete(transactionsCacheKey);
    }

    /**
     * 更新交易缓存
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void update(Set<Transaction> items){
        long startTime = System.currentTimeMillis();
        logger.debug("开始更新Redis交易缓存:timestamp({})",startTime);
        // 取出缓存中的交易总数
        long cacheItemCount = redisTemplate.opsForZSet().size(transactionsCacheKey);
        Set<ZSetOperations.TypedTuple<String>> stageSet = new HashSet<>();
        class MinMax{
            private Long minOffset=Long.MAX_VALUE;
            private Long maxOffset=Long.MIN_VALUE;
        }
        MinMax mm=new MinMax();
        items.forEach(item->{
            Long score = item.getSequence();
            if(score<mm.minOffset) mm.minOffset=score;
            if(score>mm.maxOffset) mm.maxOffset=score;
        });
        // 查询在缓存中是否有值
        Set<String> exist = redisTemplate.opsForZSet().rangeByScore(transactionsCacheKey,mm.minOffset,mm.maxOffset);
        Set<Long> existScore = new HashSet<>();
        Objects.requireNonNull(exist).forEach(item->{
            Transaction transaction = JSON.parseObject(item,Transaction.class);
            existScore.add(transaction.getSequence());
        });
        items.forEach(item -> {
            Long score = item.getSequence();
            if(existScore.contains(score)) return;
            // 在缓存中不存在的才放入缓存
            stageSet.add(new DefaultTypedTuple(JSON.toJSONString(item),score.doubleValue()));
        });
        if(!stageSet.isEmpty()){
            redisTemplate.opsForZSet().add(transactionsCacheKey, stageSet);
        }
        if(cacheItemCount>maxItemCount){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (cacheItemCount-maxItemCount)个
            redisTemplate.opsForZSet().removeRange(transactionsCacheKey,0,cacheItemCount-maxItemCount);
        }
        long endTime = System.currentTimeMillis();
        logger.debug("更新Redis交易缓存结束:timestamp({}),consume({}ms)",endTime,endTime-startTime);
    }

    /**
     * 重置交易缓存
     */
    @Override
    public void reset(boolean clearOld) {
        if(clearOld) clear();
        TransactionExample condition = new TransactionExample();
        condition.createCriteria();
        condition.setOrderByClause("block_number desc,transaction_index desc");
        for(int i=0;i<500;i++){
            PageHelper.startPage(i+1,1000);
            List<Transaction> data = transactionMapper.selectByExample(condition);
            if(data.isEmpty()) break;
            update(new HashSet<>(data));
        }
    }
}
