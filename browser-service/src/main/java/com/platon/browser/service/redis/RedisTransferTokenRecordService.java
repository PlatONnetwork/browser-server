package com.platon.browser.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

/**
 * 代币交易缓存数据
 */
@Service
public class RedisTransferTokenRecordService extends RedisService<ESTokenTransferRecord> {

    /**
     * 交易缓存key
     */
    @Value("${spring.redis.key.innerTx}")
    private String innerTxCacheKey;

    @Override
    public String getCacheKey() {
        return innerTxCacheKey;
    }

    @Override
    public void updateMinMaxScore(Set<ESTokenTransferRecord> data) {
        minMax.reset();
        data.forEach(item -> {
            Long score = item.getSeq();
            if (score < minMax.getMinOffset()) minMax.setMinOffset(score);
            if (score > minMax.getMaxOffset()) minMax.setMaxOffset(score);
        });
    }

    @Override
    public void updateExistScore(Set<String> exist) {
        Objects.requireNonNull(exist).forEach(item -> existScore.add(JSON.parseObject(item, ESTokenTransferRecord.class).getSeq()));
    }

    @Override
    public void updateStageSet(Set<ESTokenTransferRecord> data) {
        data.forEach(item -> {
            // 在缓存中不存在的才放入缓存
            if (!existScore.contains(item.getSeq()))
                stageSet.add(new DefaultTypedTuple<String>(JSON.toJSONString(item), item.getSeq().doubleValue()));
        });
    }
}
