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

import javax.validation.constraints.Min;
import java.util.*;

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
        class MinMax{Long minOffset=Long.MAX_VALUE,maxOffset=Long.MIN_VALUE;}
        MinMax mm=new MinMax();
        items.forEach(item->{
            Long score = item.getNumber();
            if(score<mm.minOffset) mm.minOffset=score;
            if(score>mm.maxOffset) mm.maxOffset=score;
        });
        // 查询在缓存中是否有值
        Set<String> exist = redisTemplate.opsForZSet().rangeByScore(blocksCacheKey,mm.minOffset,mm.maxOffset);
        Set<Long> existScore = new HashSet<>();
        Objects.requireNonNull(exist).forEach(item->{
            Block block = JSON.parseObject(item,Block.class);
            existScore.add(block.getNumber());
        });
        items.forEach(item -> {
            if(existScore.contains(item.getNumber())) return;
            // 在缓存中不存在的才放入缓存
            stageSet.add(new DefaultTypedTuple(JSON.toJSONString(item),item.getNumber().doubleValue()));
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
