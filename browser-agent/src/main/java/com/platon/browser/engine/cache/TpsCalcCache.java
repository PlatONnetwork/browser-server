package com.platon.browser.engine.cache;

import com.platon.browser.dao.entity.Block;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 交易TPS计算缓存
 */
@Component
public class TpsCalcCache {

    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    // <出块秒数,交易数>
    private Map<Long,Long> cacheMap = new HashMap<>();
    private Long now = 0L;

    public void update(Block block){
        LOCK.writeLock().lock();
        try {
            long timestamp = block.getTimestamp().getTime();
            BigDecimal seconds = BigDecimal.valueOf(timestamp).divide(BigDecimal.valueOf(1000),0, RoundingMode.CEILING);
            now = seconds.longValue();
            cacheMap.putIfAbsent(now, 0L);
            cacheMap.put(now,cacheMap.get(now)+block.getStatTxQty());
            // 离now时间差大于等于10秒的都需要清理
            Set<Long> invalid = new HashSet<>();
            cacheMap.keySet().forEach(second->{
                if(now-second>=10) invalid.add(second);
            });
            cacheMap.keySet().removeAll(invalid);
        }finally {
            LOCK.writeLock().unlock();
        }
    }

    public int getTps(){
        LOCK.readLock().lock();
        try {
            if(now==0) return 0;
            long minSecond=Long.MAX_VALUE;
            long maxSecond=Long.MIN_VALUE;
            int totalTxCount=0;
            for (Map.Entry<Long, Long> entry : cacheMap.entrySet()) {
                Long second = entry.getKey();
                Long txCount = entry.getValue();
                if (second < minSecond) minSecond = second;
                if (second > maxSecond) maxSecond = second;
                totalTxCount+=txCount;
            }
            if(totalTxCount==0||maxSecond<minSecond) return 0;
            if (maxSecond==minSecond) return totalTxCount;
            BigDecimal tps = BigDecimal.valueOf(totalTxCount).divide(BigDecimal.valueOf(maxSecond-minSecond),0,RoundingMode.FLOOR);
            return tps.intValue();
        }finally {
            LOCK.readLock().unlock();
        }
    }
}
