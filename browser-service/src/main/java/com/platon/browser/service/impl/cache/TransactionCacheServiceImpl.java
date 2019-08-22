package com.platon.browser.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.service.TransactionCacheService;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.util.RedisPipelineTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class TransactionCacheServiceImpl implements TransactionCacheService {
    private final Logger logger = LoggerFactory.getLogger(TransactionCacheServiceImpl.class);
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
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
    @Override
    public void update(Set<Transaction> items){
        long startTime = System.currentTimeMillis();
        logger.debug("开始更新Redis交易缓存:timestamp({})",startTime);
        // 取出缓存中的交易总数
        long cacheItemCount = redisTemplate.opsForZSet().size(transactionsCacheKey);
        Set<ZSetOperations.TypedTuple<String>> stageSet = new HashSet<>();
        items.forEach(item -> {
            Long startOffset=0l,endOffset=0l,score=0l;
            // 排序分数：区块号*10000+交易索引
            startOffset=endOffset=score = item.getBlockNumber()*10000+item.getTransactionIndex();
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(transactionsCacheKey,startOffset,endOffset);
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                stageSet.add(new DefaultTypedTuple(JSON.toJSONString(item),score.doubleValue()));
            }
        });
        if(stageSet.size()>0){
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
            if(data.size()==0) break;
            update(new HashSet<>(data));
        }
    }


}
