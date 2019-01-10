package com.platon.browser.schedule;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomStatisticsMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.*;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionPushItem;
import com.platon.browser.enums.NodeType;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.RedisCacheService;
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

import java.math.BigDecimal;
import java.util.*;

@Component
public class StompPushTask {

    private final Logger logger = LoggerFactory.getLogger(StompPushTask.class);

    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CustomStatisticsMapper customStatisticsMapper;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private I18nUtil i18n;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${platon.redis.key.maxtps}")
    private String maxtpsCacheKeyTemplate;
    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private NodeService nodeService;

    /**
     * 推送节点信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushNode(){
        chainsConfig.getChainIds().forEach(chainId -> {
            // 从redis缓存获取节点信息，全量推送节点信息
            List<NodePushItem> nodeCache = nodeService.getPushData(chainId);
            BaseResp nodeResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodeCache);
            messagingTemplate.convertAndSend("/topic/node/new?cid="+chainId, nodeResp);
        });
    }

    /**
     * 推送指标信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushIndex(){
        chainsConfig.getChainIds().forEach(chainId -> {
            IndexInfo index = new IndexInfo();

            /*************设置当前块高、出块节点*************/
            RespPage<BlockListItem> page = redisCacheService.getBlockPage(chainId,1,1);
            if(page.getData().size()!=0){
                List<BlockListItem> blocks = page.getData();
                index.setCurrentHeight(blocks.get(0).getHeight());
                index.setNode(blocks.get(0).getMiner());
            }

            /*************设置共识节点数*************/
            List<NodePushItem> nodes = nodeService.getPushData(chainId);
            int consensusCount = 0;
            for (NodePushItem node:nodes){
                switch (NodeType.getEnum(node.getNodeType())){
                    case CONSENSUS: consensusCount++;
                }
            }
            index.setConsensusNodeAmount(consensusCount);

            /*************设置交易笔数***********/
            TransactionExample te = new TransactionExample();
            te.createCriteria().andChainIdEqualTo(chainId);
            long transactionCount = transactionMapper.countByExample(te);
            index.setCurrentTransaction(transactionCount);

            /*************设置地址数*************/
            long addressCount = customStatisticsMapper.countAddress(chainId);
            index.setAddressAmount(addressCount);
            BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),index);
            messagingTemplate.convertAndSend("/topic/index/new?cid="+chainId, resp);
        });
    }


    /**
     * 推送统计相关信息
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void pushStatistic(){
        chainsConfig.getChainIds().forEach(chainId -> {
            StatisticInfo statistic = new StatisticInfo();

            /************** 计算当前TPS ************/
            // 以缓存中最新的块的时间为参考，查询滞后一秒的所有区块，累计其交易数作为当前TPS
            RespPage<BlockListItem> blockPage = redisCacheService.getBlockPage(chainId,1,1);
            List<BlockListItem> blocks = blockPage.getData();
            if(blocks.size()>0){
                BlockListItem bli = blocks.get(0);
                Date endDate = new Date(bli.getTimestamp());
                Date startDate = new Date(bli.getTimestamp()-1000);

                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(chainId).andTimestampBetween(startDate,endDate);
                List<Block> blockList=blockMapper.selectByExample(blockExample);

                statistic.setCurrent(0);
                if(blockList.size()>0){
                    blockList.forEach(block -> statistic.setCurrent(statistic.getCurrent()+block.getTransactionNumber()));
                }
            }

            /************** 计算平均出块时长 *************/
            // 区块按块号倒排，取第3600个块
            String blockCacheKey = blockCacheKeyTemplate.replace("{}",chainId);
            Set<String> oldest = redisTemplate.opsForZSet().reverseRange(blockCacheKey,3599,3599);
            Set<String> newest = redisTemplate.opsForZSet().reverseRange(blockCacheKey,0,0);
            if(oldest.size()==0){
                // 总共不足3600个块，则正向取第一个
                oldest = redisTemplate.opsForZSet().range(blockCacheKey,0,0);
            }
            long highestBlockTimestamp=0,lowestBlockTimestamp=0,highestBlockNumber = 1, lowestBlockNumber = 0;
            if(oldest.size()!=0){
                Block oldestBlock = JSON.parseObject(oldest.iterator().next(),Block.class);
                lowestBlockNumber=oldestBlock.getNumber();
                lowestBlockTimestamp=oldestBlock.getTimestamp().getTime();
            }
            if(newest.size()!=0){
                Block newestBlock = JSON.parseObject(newest.iterator().next(),Block.class);
                highestBlockNumber=newestBlock.getNumber();
                highestBlockTimestamp=newestBlock.getTimestamp().getTime();
            }

            long divider = highestBlockNumber-lowestBlockNumber;
            if(divider==0){
                divider=1;
            }
            divider = divider*1000;
            BigDecimal avgTime=BigDecimal.valueOf(highestBlockTimestamp-lowestBlockTimestamp).divide(BigDecimal.valueOf(divider),4,BigDecimal.ROUND_HALF_UP);
            statistic.setAvgTime(avgTime);

            /************** 计算最大TPS ************/
            // 取缓存中的最大交易TPS与当前TPS比较，把最大的TPS更新到缓存中
            String cacheKey = maxtpsCacheKeyTemplate.replace("{}",chainId);
            String maxtpsStr = redisTemplate.opsForValue().get(cacheKey);
            long maxtpsLong = 0;
            if(StringUtils.isNotBlank(maxtpsStr)){
                maxtpsLong = Long.valueOf(maxtpsStr);
            }
            statistic.setMaxTps(maxtpsLong);
            if(maxtpsLong<statistic.getCurrent()){
                statistic.setMaxTps(statistic.getCurrent());
                redisTemplate.opsForValue().set(cacheKey,String.valueOf(statistic.getMaxTps()));
            }

            /************** 计算平均区块交易数 ************/
            // 最新3600个块的平均区块交易数
            BigDecimal avgBlockTrans = customStatisticsMapper.countAvgTransactionPerBlock(chainId);
            statistic.setAvgTransaction(avgBlockTrans!=null?avgBlockTrans:BigDecimal.ZERO);

            /************** 计算24小时内的交易数 ************/
            long dayTransactionCount = customStatisticsMapper.countTransactionIn24Hours(chainId);
            statistic.setDayTransaction(dayTransactionCount);

            /************** 组装图表数据 ************/
            List<StatisticPushItem> items = redisCacheService.getStatisticPushData(chainId,1,50);
            StatisticGraphData graphData = new StatisticGraphData();
            for (int i=0;i<items.size();i++){
                StatisticPushItem item = items.get(i);
                if(i==0||i==items.size()-1) continue;
                StatisticPushItem prevItem = items.get(i-1);
                graphData.getX().add(item.getHeight());
                graphData.getYa().add((item.getTime()-prevItem.getTime())/1000);
                graphData.getYb().add(item.getTransaction()==null?0:item.getTransaction());
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
            List<BlockPushItem> blocks = redisCacheService.getBlockPushData(chainId,1,10);
            BaseResp blockResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blocks);
            messagingTemplate.convertAndSend("/topic/block/new?cid="+chainId, blockResp);

            // 全量推送交易信息
            List<TransactionPushItem> transactions = redisCacheService.getTransactionPushData(chainId,1,10);
            BaseResp transactionResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactions);
            messagingTemplate.convertAndSend("/topic/transaction/new?cid="+chainId, transactionResp);
        });
    }
}
