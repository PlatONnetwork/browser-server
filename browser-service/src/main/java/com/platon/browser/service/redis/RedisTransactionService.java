package com.platon.browser.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * 交易缓存数据处理逻辑\
 */
@Service
public class RedisTransactionService extends AbstractRedisService<Transaction> {
    @Override
    public String getCacheKey() {
        return redisKeyConfig.getTransactions();
    }

    @Override
    public void updateMinMaxScore(Set<Transaction> data) {
        minMax.reset();
        data.forEach(item->{
            Long score = item.getSeq();
            if(score<minMax.getMinOffset()) minMax.setMinOffset(score);
            if(score>minMax.getMaxOffset()) minMax.setMaxOffset(score);
        });
    }

    @Override
    public void updateExistScore(Set<String> exist) {
        Objects.requireNonNull(exist).forEach(item->existScore.add(JSON.parseObject(item, Transaction.class).getSeq()));
    }

    @Override
    public void updateStageSet(Set<Transaction> data) {
        data.forEach(item -> {
            // 在缓存中不存在的才放入缓存
            if(!existScore.contains(item.getSeq())) stageSet.add(new DefaultTypedTuple<String>(JSON.toJSONString(item),item.getSeq().doubleValue()));
        });
    }
}
