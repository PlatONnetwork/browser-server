package com.platon.browser.schedule;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@Component
public class UpdateCacheTask {

    private final Logger logger = LoggerFactory.getLogger(UpdateCacheTask.class);

    @Autowired
    private ChainsConfig chainsConfig;

    // 保存每条链的上次TPS统计的时间戳
    private Map<String,Long> prevTimestampMap = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CacheService cacheService;

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

            // 取当前日期作为查询交易记录的结束时间
            Date endDate = new Date(currentTimestamp);
            // 默认取上一次统计时间作为查询交易记录的开始时间
            Date startDate = new Date(prevTimestamp);
            if(prevTimestamp==0||(currentTimestamp-prevTimestamp)>intervalInMillisecond){
                // 如果上次统计时间戳为0，或者当前时间戳与上次统计时间戳之差超出指定的统计间隔，
                // 则取【当前时刻回溯一个间隔时间】到【当前时刻】的交易来统计交易TPS
                // 取得指定时间范围内的交易记录，判断第一条和最后一条记录的
                startDate = new Date(currentTimestamp-intervalInMillisecond); // 当前日期往前推一个间隔的日期
            }

            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId)
                    .andTimestampBetween(startDate,endDate);
            List<Transaction> transactionList = transactionMapper.selectByExample(condition);
            int transactionCount = transactionList.size();
            statisticInfo.setTransactionCount(Long.valueOf(transactionCount));
            statisticInfo.setCurrent(Long.valueOf(transactionCount));
            BigDecimal transactionTps = BigDecimal.valueOf(transactionCount).divide(BigDecimal.valueOf(intervalInSecond),4,BigDecimal.ROUND_DOWN);
            statisticInfo.setMaxTps(transactionTps.doubleValue());
            cacheService.updateStatisticInfo(statisticInfo,false,chainId);


            // 更新统计时间戳
            prevTimestampMap.put(chainId,currentTimestamp);
        });
    }

    /**
     * 推送统计信息，1秒推送一次
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushStatisticInfo(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 推送整体统计信息
            StatisticInfo statisticInfo = cacheService.getStatisticInfo(chainId);
            statisticInfo.setBlockStatisticList(statisticInfo.getLimitQueue().elementsAsc());
            BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),statisticInfo);
            messagingTemplate.convertAndSend("/topic/statistic/new?cid="+chainId, resp);
        });
    }
}
