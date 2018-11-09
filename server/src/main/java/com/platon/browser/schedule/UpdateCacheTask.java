package com.platon.browser.schedule;

import com.platon.browser.cache.CacheInitializer;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
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

    @Autowired
    private CacheInitializer cacheInitializer;

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

    /**
     * 检查缓存，如果发现缓存为空，则初始化
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void initCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<NodeInfo> nodeInfoList = cacheService.getNodeInfoList(chainId);
            if(nodeInfoList.size()==0){
                logger.info("节点缓存为空, 执行初始化...");
                cacheInitializer.initNodeInfoList(chainId);
            }
            IndexInfo indexInfo = cacheService.getIndexInfo(chainId);
            if(indexInfo.getCurrentHeight()==0){
                logger.info("指标缓存为空, 执行初始化...");
                cacheInitializer.initIndexInfo(chainId);
            }
            StatisticInfo statisticInfo = cacheService.getStatisticInfo(chainId);
            if(statisticInfo.getLowestBlockNumber()==null||statisticInfo.getLowestBlockNumber()==0){
                logger.info("统计缓存为空, 执行初始化...");
                cacheInitializer.initStatisticInfo(chainId);
            }
            List<BlockInfo> blockInfoList = cacheService.getBlockInfoList(chainId);
            if(blockInfoList.size()==0){
                logger.info("区块缓存为空, 执行初始化...");
                cacheInitializer.initBlockInfoList(chainId);
            }
            List<TransactionInfo> transactionInfoList = cacheService.getTransactionInfoList(chainId);
            if(transactionInfoList.size()==0){
                logger.info("交易缓存为空, 执行初始化...");
                cacheInitializer.initTransactionInfoList(chainId);
            }
        });
    }
}
