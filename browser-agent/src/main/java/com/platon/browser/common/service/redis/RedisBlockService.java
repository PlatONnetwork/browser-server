package com.platon.browser.common.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 区块缓存数据处理逻辑
 * @Auther: Chendongming
 * @Date: 2019/8/21 09:47
 * @Description:
 */
@Slf4j
@Service
public class RedisBlockService implements RedisService<Block> {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /** 区块缓存key */
    @Value("${spring.redis.key.blocks}")
    private String blocksCacheKey;
    /** 最大数据 */
    @Value("${spring.redis.max-item}")
    private long maxItemCount;

    /**
     * 清除区块缓存
     */
    public void clear() {
        redisTemplate.delete(blocksCacheKey);
    }

    /**
     * 更新区块缓存
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void save(Set<Block> items){
        long startTime = System.currentTimeMillis();
        log.debug("开始更新Redis区块缓存:timestamp({})",startTime);
        // 取出缓存中的区块总数
        long cacheItemCount = redisTemplate.opsForZSet().size(blocksCacheKey);
        // 待入库列表
        Set<ZSetOperations.TypedTuple<String>> stageSet = new HashSet<>();
        class MinMax{
            private Long minOffset=Long.MAX_VALUE;
            private Long maxOffset=Long.MIN_VALUE;
        }
        MinMax mm=new MinMax();
        items.forEach(item->{
            Long score = item.getNum();
            if(score<mm.minOffset) mm.minOffset=score;
            if(score>mm.maxOffset) mm.maxOffset=score;
        });
        // 查询在缓存中是否有值
        Set<String> exist = redisTemplate.opsForZSet().rangeByScore(blocksCacheKey,mm.minOffset,mm.maxOffset);
        Set<Long> existScore = new HashSet<>();
        Objects.requireNonNull(exist).forEach(item->{
            Block block = JSON.parseObject(item,Block.class);
            existScore.add(block.getNum());
        });
        items.forEach(item -> {
            if(existScore.contains(item.getNum())) return;
            // 在缓存中不存在的才放入缓存
            stageSet.add(new DefaultTypedTuple(JSON.toJSONString(item),item.getNum().doubleValue()));
        });
        if(!stageSet.isEmpty()){
            redisTemplate.opsForZSet().add(blocksCacheKey, stageSet);
        }
        if(cacheItemCount>maxItemCount){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (cacheItemCount-maxItemCount)个
            redisTemplate.opsForZSet().removeRange(blocksCacheKey,0,cacheItemCount-maxItemCount);
        }
        long endTime = System.currentTimeMillis();
        log.debug("更新Redis区块缓存结束:timestamp({}),consume({}ms)",endTime,endTime-startTime);
    }

    /**
     * 重置区块缓存
     */
    /*public void reset(boolean clearOld) {
        if(clearOld) clear();
        BlockExample condition = new BlockExample();
        condition.setOrderByClause("number desc");
        for(int i=0;i<1000;i++){
            PageHelper.startPage(i+1,500);
            List<Block> data = blockMapper.selectByExample(condition);
            if(data.isEmpty()) break;
            update(new HashSet<>(data));
        }
    }*/
}
