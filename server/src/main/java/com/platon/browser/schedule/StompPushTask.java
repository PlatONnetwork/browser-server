package com.platon.browser.schedule;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.dto.StatisticsCache;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticGraphData;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticPushItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionPushItem;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StompPushTask {

    private final Logger logger = LoggerFactory.getLogger(StompPushTask.class);

    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private I18nUtil i18n;
    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    @Autowired
    private NodeService nodeService;

    /**
     * 推送节点信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushNode(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 从redis缓存获取节点信息，全量推送节点信息
            List<NodePushItem> nodeCache = nodeService.getPushCache(chainId);
            BaseResp nodeResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodeCache);
            messagingTemplate.convertAndSend("/topic/node/new?cid="+chainId, nodeResp);
        });
    }

    IndexInfo prevIndex;
    /**
     * 推送指标相关信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushIndex(){
        chainsConfig.getChainIds().forEach(chainId -> {
            StatisticsCache cache = redisCacheService.getStatisticsCache(chainId);
            if((prevIndex!=null&&(prevIndex.getCurrentHeight() < cache.getCurrentHeight()))||prevIndex==null){
                // 如果前一次指标为null,或前一次指标的块高小于当前缓存块高，则推送
                IndexInfo index = new IndexInfo();
                BeanUtils.copyProperties(cache,index);
                index.setConsensusNodeAmount(cache.getConsensusCount());
                index.setCurrentTransaction(cache.getTransactionCount());
                index.setAddressAmount(cache.getAddressCount());
                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),index);
                messagingTemplate.convertAndSend("/topic/index/new?cid="+chainId, resp);
                prevIndex=index;
            }
        });
    }

    /**
     * 推送统计相关信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushStatistics(){
        chainsConfig.getChainIds().forEach(chainId -> {
            StatisticsCache cache = redisCacheService.getStatisticsCache(chainId);
            StatisticInfo statistic = new StatisticInfo();
            BeanUtils.copyProperties(cache,statistic);
            /************** 组装图表数据 ************/
            List<StatisticPushItem> items = redisCacheService.getStatisticPushCache(chainId,1,50);
            StatisticGraphData graphData = new StatisticGraphData();
            for (int i=0;i<items.size();i++){
                StatisticPushItem currentBlock = items.get(i);
                if(i==0||i==items.size()-1) continue;
                StatisticPushItem previousBlock = items.get(i-1);
                graphData.getX().add(currentBlock.getHeight());
                BigDecimal sec = BigDecimal.valueOf(currentBlock.getTime()-previousBlock.getTime()).divide(BigDecimal.valueOf(1000));
                graphData.getYa().add(sec.doubleValue());
                graphData.getYb().add(currentBlock.getTransaction()==null?0:currentBlock.getTransaction());
            }
            statistic.setGraphData(graphData);
            BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),statistic);
            messagingTemplate.convertAndSend("/topic/statistic/new?cid="+chainId, resp);
        });
    }

    /**
     * 推送区块相关信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushBlock(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 全量推送区块信息
            List<BlockPushItem> blocks = redisCacheService.getBlockPushCache(chainId,1,10);
            BaseResp blockResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blocks);
            messagingTemplate.convertAndSend("/topic/block/new?cid="+chainId, blockResp);
        });
    }

    /**
     * 推送交易相关信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushTransaction(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 全量推送交易信息
            List<TransactionPushItem> transactions = redisCacheService.getTransactionPushCache(chainId,1,10);
            BaseResp transactionResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactions);
            messagingTemplate.convertAndSend("/topic/transaction/new?cid="+chainId, transactionResp);
        });
    }
}
