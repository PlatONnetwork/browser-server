package com.platon.browser.schedule;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.service.StompCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CacheUpdateTask {

    private final Logger logger = LoggerFactory.getLogger(CacheUpdateTask.class);

    @Autowired
    private ChainsConfig chainsConfig;

    // 保存每条链的上次TPS统计的时间戳
    private Map<String,Long> prevTimestampMap = new HashMap<>();
    @Autowired
    private StompCacheService cacheService;
    @Autowired
    private TransactionMapper transactionMapper;

    // 交易TPS统计时间间隔, 单位：分钟
    @Value("${platon.transaction.tps.statistic.interval}")
    private int transactionTpsStatisticInterval;

    @PostConstruct
    public void init(){
        chainsConfig.getChainIds().forEach(chainId -> prevTimestampMap.put(chainId,0l));
    }

    /**
     * 更新交易TPS
     */
    @Scheduled(cron="${platon.transaction.tps.statistic.cron}")
    public void updateTransactionTps(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 当前链的上次统计时间戳
            long prevTimestamp = prevTimestampMap.get(chainId);
            // 当前时间戳
            long currentTimestamp = System.currentTimeMillis();
            // 间隔时间的秒表示
            long intervalInSecond = transactionTpsStatisticInterval*60;
            // 间隔时间毫秒表示
            long intervalInMillisecond = intervalInSecond*1000;

            StatisticInfo statisticInfo = new StatisticInfo();

            logger.info("统计链【ID={}】交易TPS",chainId);

            // 默认取上一次统计时间作为查询交易记录的开始时间
            Date startDate = new Date(prevTimestamp);
            // 取上次统计时间戳+intervalInMillisecond日期作为查询交易记录的结束时间
            long tmp = prevTimestamp+intervalInMillisecond;
            Date endDate = new Date(tmp);
            // 计算TPS时默认使用设置的间隔的秒数作为除数
            long divisor = intervalInSecond;
            if(tmp>currentTimestamp){
                // 上次时间加上间隔时间大于当前时间，则取当前时间为结束时间
                endDate = new Date(currentTimestamp);
                // 使用实际的时间间隔作为除数
                divisor = (currentTimestamp-prevTimestamp)/1000;
            }

            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId)
                    .andTimestampBetween(startDate,endDate);
            List<Transaction> transactionList = transactionMapper.selectByExample(condition);
            int transactionCount = transactionList.size();
            statisticInfo.setTransactionCount(Long.valueOf(transactionCount));
            statisticInfo.setCurrent(Long.valueOf(transactionCount));
            if(divisor!=0){
                BigDecimal transactionTps = BigDecimal.valueOf(transactionCount).divide(BigDecimal.valueOf(divisor),4,BigDecimal.ROUND_DOWN);
                statisticInfo.setMaxTps(transactionTps.doubleValue());
            }
            cacheService.updateStatisticCache(statisticInfo,false,chainId);

            // 更新统计时间戳
            prevTimestampMap.put(chainId,currentTimestamp);
        });
    }

}
