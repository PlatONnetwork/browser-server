package com.platon.browser.schedule;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.StatisticMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticGraphData;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.cache.BlockInit;
import com.platon.browser.dto.cache.LimitQueue;
import com.platon.browser.dto.cache.NodeIncrement;
import com.platon.browser.dto.cache.TransactionInit;
import com.platon.browser.service.StompCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class StompPushTask {

    private final Logger logger = LoggerFactory.getLogger(StompPushTask.class);
    // 保存每条链的上次TPS统计的时间戳
    private Map<String,Long> prevTimestampMap = new HashMap<>();
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private StompCacheService cacheService;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private I18nUtil i18n;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${platon.redis.maxtps.cache.key}")
    private String maxtpsCacheKeyTemplate;

    // 交易TPS统计时间间隔, 单位：分钟
    @Value("${platon.transaction.tps.statistic.interval}")
    private int transactionTpsStatisticInterval;

    @PostConstruct
    public void init(){
        chainsConfig.getChainIds().forEach(chainId -> prevTimestampMap.put(chainId,0l));
    }

    /**
     * 首页信息，1秒推送一次
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void push(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 增量推送节点信息，1秒推送一次
            NodeIncrement nodeIncrement = cacheService.getNodeIncrement(chainId);
            if(nodeIncrement.isChanged()){
                logger.debug("节点增量缓存有变更，推送STOMP消息...");
                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodeIncrement.getIncrement());
                messagingTemplate.convertAndSend("/topic/node/new?cid="+chainId, resp);
            }

            // 全量推送区块信息，1秒推送一次
            BlockInit blockInit = cacheService.getBlockInit(chainId);
            BaseResp blockResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockInit.getList());
            messagingTemplate.convertAndSend("/topic/block/new?cid="+chainId, blockResp);

            // 全量推送交易信息，1秒推送一次
            TransactionInit transactionInit = cacheService.getTransactionInit(chainId);
            BaseResp transactionResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactionInit.getList());
            messagingTemplate.convertAndSend("/topic/transaction/new?cid="+chainId, transactionResp);

            IndexInfo index = cacheService.getIndexInfo(chainId);
            if(index.isChanged()){
                logger.debug("指标缓存有变更，推送STOMP消息...");
                // 取地址数
                long addressCount = statisticMapper.countAddress(chainId);
                index.setAddressAmount(addressCount);
                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),index);
                messagingTemplate.convertAndSend("/topic/index/new?cid="+chainId, resp);
            }

            StatisticInfo statistic = cacheService.getStatisticInfo(chainId);

            // 当前TPS
            long endStamp = statistic.getHighestBlockTimestamp();
            Date endDate = new Date(endStamp);
            long startStamp = statistic.getHighestBlockTimestamp()-1000;
            Date startDate = new Date(startStamp);

            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainId)
                    .andTimestampBetween(startDate,endDate);
            List<Block> blockList=blockMapper.selectByExample(blockExample);

            statistic.setCurrent(0);
            if(blockList.size()>0){
                blockList.forEach(block -> {
                    statistic.setCurrent(statistic.getCurrent()+block.getTransactionNumber());
                });
            }

            // 最大TPS
            String cacheKey = maxtpsCacheKeyTemplate.replace("{}",chainId);
            String maxtpsStr = redisTemplate.opsForValue().get(cacheKey);
            long maxtpsLong = 0;
            if(StringUtils.isNotBlank(maxtpsStr)){
                maxtpsLong = Long.valueOf(maxtpsStr);
            }
            statistic.setMaxTps(maxtpsLong);
            if(maxtpsLong<statistic.getCurrent()){
                statistic.setMaxTps(statistic.getCurrent());
                redisTemplate.opsForValue().set(cacheKey,String.valueOf(statistic.getCurrent()));
            }


            if(statistic.isChanged()){
                logger.debug("统计缓存有变更，推送STOMP消息...");

                // 取24小时内的交易数
                long dayTransactionCount = statisticMapper.countTransactionIn24Hours(chainId);
                statistic.setDayTransaction(dayTransactionCount);

                LimitQueue<StatisticItem> limitQueue = statistic.getLimitQueue();
                List<StatisticItem> itemList = limitQueue.list();
                Collections.sort(itemList,(c1, c2)->{
                    // 按区块高度正排
                    if(c1.getHeight()>c2.getHeight()) return 1;
                    if(c1.getHeight()<c2.getHeight()) return -1;
                    return 0;
                });

                StatisticGraphData graphData = new StatisticGraphData();
                for (int i=0;i<itemList.size();i++){
                    StatisticItem item = itemList.get(i);
                    if(i==0||i==itemList.size()-1) continue;
                    StatisticItem prevItem = itemList.get(i-1);
                    graphData.getX().add(item.getHeight());
                    graphData.getYa().add((item.getTime()-prevItem.getTime())/1000);
                    graphData.getYb().add(item.getTransaction()==null?0:item.getTransaction());
                }
                statistic.setGraphData(graphData);

                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),statistic);
                messagingTemplate.convertAndSend("/topic/statistic/new?cid="+chainId, resp);
            }
        });
    }
}
