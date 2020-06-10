package com.platon.browser.common.complement.cache;

import com.platon.browser.elasticsearch.dto.Block;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 交易TPS计算缓存
 */
@Component
public class TpsCalcCache {

    // <出块秒数,交易数>
    private Map<Long,Integer> cacheMap = new HashMap<>();
    
    private Map<Long,Integer> maxCacheMap = new HashMap<>();

    private int tps=0;
    
    private int maxTps=0;
    
    private static Long startTime = 0l;
    
    private static Long maxTime = 0l;

    public void update(Block block){
        BigDecimal seconds = BigDecimal.valueOf(block.getTime().getTime()).divide(BigDecimal.valueOf(1000),0, RoundingMode.CEILING);
        Long now = seconds.longValue();
        Integer txQty = cacheMap.get(TpsCalcCache.startTime);
        if(txQty == null) {
        	txQty = 0;
        	TpsCalcCache.startTime = now;
        	cacheMap.putIfAbsent(TpsCalcCache.startTime, 0);
        }
        if(now <= startTime + 10) {
        	txQty += block.getTransactions().size();
        	cacheMap.put(startTime, txQty);
		} else {
			tps = BigDecimal.valueOf(txQty).divide(BigDecimal.TEN,0,RoundingMode.CEILING).intValue();
			startTime = startTime+10;
			cacheMap.clear();
			cacheMap.put(startTime, 0);
		}
        
        
        Integer maxTxQty = maxCacheMap.get(TpsCalcCache.maxTime);
        if(maxTxQty == null) {
        	maxTxQty = 0;
        	TpsCalcCache.maxTime = now;
        	maxCacheMap.putIfAbsent(TpsCalcCache.maxTime, 0);
        }
        if(TpsCalcCache.maxTime.intValue() == now.intValue()) {
        	maxTxQty +=  block.getTransactions().size();
        	maxCacheMap.put(TpsCalcCache.maxTime, maxTxQty);
        } else {
        	maxCacheMap.clear();
        	maxCacheMap.put(TpsCalcCache.maxTime, maxTxQty);
        }
        maxTps = maxTxQty.intValue();
        
//        cacheMap.putIfAbsent(now, 0L);
//        cacheMap.put(now,cacheMap.get(now)+block.getTransactions().size());
//        // 离now时间差大于等于10秒的都需要清理
//        Set<Long> invalid = new HashSet<>();
//        cacheMap.keySet().forEach(second->{
//            if(now-second>=10) invalid.add(second);
//        });
//        cacheMap.keySet().removeAll(invalid);
//
//        if(now==0) return;
//
//        long minSecond=Long.MAX_VALUE;
//        long maxSecond=Long.MIN_VALUE;
//        int totalTxCount=0;
//        for (Map.Entry<Long, Long> entry : cacheMap.entrySet()) {
//            Long second = entry.getKey();
//            Long txCount = entry.getValue();
//            if (second < minSecond) minSecond = second;
//            if (second > maxSecond) maxSecond = second;
//            totalTxCount+=txCount;
//        }
//        if(totalTxCount==0){
//            tps=0;
//            return;
//        }
//
//        tps = BigDecimal.valueOf(totalTxCount).divide(BigDecimal.valueOf(maxSecond-minSecond+1),0,RoundingMode.CEILING).intValue();
    }

    public int getTps(){
        return tps;
    }

	public int getMaxTps() {
		return maxTps;
	}
    
}
