package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.service.BlockCacheService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 09:47
 * @Description:
 */
@Service
public class BlockCacheServiceImpl implements BlockCacheService {
    private final Logger logger = LoggerFactory.getLogger(BlockCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${platon.redis.key.blocks}")
    private String blocksCacheKey;
    @Value("${platon.redis.max-item}")
    private long maxItemCount;
    @Autowired
    private BlockMapper blockMapper;

    /**
     * 清除区块缓存
     */
    @Override
    public void clear() {
        redisTemplate.delete(blocksCacheKey);
    }

    /**
     * 更新区块缓存
     */
    @Override
    public void update(Set<Block> items){
        long startTime = System.currentTimeMillis();
        logger.debug("开始更新Redis区块缓存:timestamp({})",startTime);
        // 取出缓存中的区块总数
        long cacheItemCount = redisTemplate.opsForZSet().size(blocksCacheKey);
        // 待入库列表
        Set<ZSetOperations.TypedTuple<String>> stageSet = new HashSet<>();
        items.forEach(item -> {
            Long startOffset=0l,endOffset=0l,score=0l;
            startOffset=endOffset=score = item.getNumber();
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(blocksCacheKey,startOffset,endOffset);
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                stageSet.add(new DefaultTypedTuple(JSON.toJSONString(item),score.doubleValue()));
            }
        });
        if(stageSet.size()>0){
            redisTemplate.opsForZSet().add(blocksCacheKey, stageSet);
        }
        if(cacheItemCount>maxItemCount){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (cacheItemCount-maxItemCount)个
            redisTemplate.opsForZSet().removeRange(blocksCacheKey,0,cacheItemCount-maxItemCount);
        }
        long endTime = System.currentTimeMillis();
        logger.debug("更新Redis区块缓存结束:timestamp({}),consume({}ms)",endTime,endTime-startTime);
    }

    /**
     * 重置区块缓存
     */
    @Override
    public void reset(boolean clearOld) {
        if(clearOld) clear();
        BlockExample condition = new BlockExample();
        condition.setOrderByClause("number desc");
        for(int i=0;i<1000;i++){
            PageHelper.startPage(i+1,500);
            List<Block> data = blockMapper.selectByExample(condition);
            if(data.size()==0) break;
            update(new HashSet<>(data));
        }
    }
}
