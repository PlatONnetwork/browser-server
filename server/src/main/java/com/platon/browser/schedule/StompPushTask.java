package com.platon.browser.schedule;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.mapper.CustomStatisticsMapper;
import com.platon.browser.dto.*;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.cache.BlockInit;
import com.platon.browser.dto.cache.LimitQueue;
import com.platon.browser.dto.cache.TransactionInit;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.service.StompCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CustomStatisticsMapper customStatisticsMapper;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private I18nUtil i18n;

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
            /*// 增量推送节点信息，1秒推送一次
            NodeIncrement nodeIncrement = cacheService.getNodeIncrement(chainId);
            if(nodeIncrement.isChanged()){
                logger.info("节点增量缓存有变更，推送STOMP消息...");
                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodeIncrement.getIncrement());
                messagingTemplate.convertAndSend("/topic/node/new?cid="+chainId, resp);
            }*/

            // 从redis缓存获取节点信息，全量推送节点信息
            List<NodeInfo> nodeCache = redisCacheService.getNodeList(chainId);
            BaseResp nodeResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodeCache);
            messagingTemplate.convertAndSend("/topic/node/new?cid="+chainId, nodeResp);

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
                logger.info("指标缓存有变更，推送STOMP消息...");
                // 取地址数
                long addressCount = customStatisticsMapper.countAddress(chainId);
                index.setAddressAmount(addressCount);
                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),index);
                messagingTemplate.convertAndSend("/topic/index/new?cid="+chainId, resp);
            }

            StatisticInfo statistic = cacheService.getStatisticInfo(chainId);
            if(statistic.isChanged()){
                logger.info("统计缓存有变更，推送STOMP消息...");

                // 取24小时内的交易数
                long dayTransactionCount = customStatisticsMapper.countTransactionIn24Hours(chainId);
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




                // 平均区块交易数=统计最近3600个区块的平均交易数
                // 取最近3600个区块，平均出块时长=(最高块出块时间-最低块出块时间)/实际块数量
                RespPage<BlockItem> blockPage = redisCacheService.getBlockPage(chainId,1,3600);
                int bCount = 0, tCount = 0;
                if(blockPage.getData()==null||blockPage.getData().size()==0){
                    // 设置平均区块交易数
                    statistic.setAvgTransaction(BigDecimal.ZERO);
                    // 设置平均出块时长
                    statistic.setAvgTime(BigDecimal.ZERO);
                }else{
                    // 设置平均区块交易数
                    List<BlockItem> blockItems = blockPage.getData();
                    for (BlockItem item : blockItems){
                        tCount+=item.getTransaction();
                    }
                    bCount=blockItems.size();
                    if(bCount==0) bCount=1;
                    BigDecimal avgTransaction = new BigDecimal(tCount).divide(new BigDecimal(bCount),4, RoundingMode.DOWN);
                    statistic.setAvgTransaction(avgTransaction);

                    // 设置平均出块时长
                    BlockItem top = blockItems.get(0);
                    BlockItem bot = blockItems.get(blockItems.size()-1);
                    if(top==bot) {
                        statistic.setAvgTime(BigDecimal.ZERO);
                    }else{
                        long diff = top.getTimestamp()-bot.getTimestamp();
                        BigDecimal avgTime = new BigDecimal(diff).divide(new BigDecimal(bCount*1000),4,RoundingMode.DOWN);
                        statistic.setAvgTime(avgTime);
                        statistic.setLowestBlockTimestamp(bot.getTimestamp());
                    }
                }



                BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),statistic);
                messagingTemplate.convertAndSend("/topic/statistic/new?cid="+chainId, resp);
            }
        });
    }
}
