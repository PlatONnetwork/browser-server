//package com.platon.browser.job;
//
//import com.platon.browser.config.ChainsConfig;
//import com.platon.browser.dto.IndexInfo;
//import com.platon.browser.dto.StatisticInfo;
//import com.platon.browser.dto.block.BlockPushItem;
//import com.platon.browser.dto.node.NodePushItem;
//import com.platon.browser.dto.transaction.TransactionPushItem;
//import com.platon.browser.enums.I18nEnum;
//import com.platon.browser.enums.RetEnum;
//import com.platon.browser.res.BaseResp;
//import com.platon.browser.service.NodeService;
//import com.platon.browser.service.StatisticService;
//import com.platon.browser.service.cache.BlockCacheService;
//import com.platon.browser.service.cache.TransactionCacheService;
//import com.platon.browser.util.I18nUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//import static com.platon.browser.service.impl.cache.NodeCacheServiceImpl.NODEID_TO_FAKE_NODES;
//
//
//@Component
//public class StompPushJob {
//
//    private final Logger logger = LoggerFactory.getLogger(StompPushJob.class);
//
//    @Autowired
//    private ChainsConfig chainsConfig;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    @Autowired
//    private BlockCacheService blockCacheService;
//    @Autowired
//    private TransactionCacheService transactionCacheService;
//    @Autowired
//    private I18nUtil i18n;
//    @Value("${platon.redis.key.block}")
//    private String blockCacheKeyTemplate;
//    @Autowired
//    private NodeService nodeService;
//    @Autowired
//    private StatisticService statisticService;
//
//    /**
//     * 推送节点信息
//     */
//    @Scheduled(cron="0/1 * * * * ?")
//    public void pushNode(){
//        chainsConfig.getChainIds().forEach(chainId -> {
//            // 从redis缓存获取节点信息，全量推送节点信息
//            List<NodePushItem> cache = nodeService.getPushCache(chainId);
//            cache.forEach(node->{
//                NodePushItem fake = NODEID_TO_FAKE_NODES.get(node.getNodeId());
//                if(fake!=null){
//                    node.setLongitude(fake.getLongitude());
//                    node.setLatitude(fake.getLatitude());
//                }
//            });
//            BaseResp nodeResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),cache);
//            messagingTemplate.convertAndSend("/topic/node/new?cid="+chainId, nodeResp);
//        });
//    }
//
//    /**
//     * 推送指标相关信息
//     */
//    @Scheduled(cron="0/1 * * * * ?")
//    public void pushIndex(){
//        chainsConfig.getChainIds().forEach(chainId -> {
//            IndexInfo index = statisticService.getIndexInfo(chainId);
//            BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),index);
//            messagingTemplate.convertAndSend("/topic/index/new?cid="+chainId, resp);
//        });
//    }
//
//    /**
//     * 推送统计相关信息
//     */
//    @Scheduled(cron="0/1 * * * * ?")
//    public void pushStatistics(){
//        chainsConfig.getChainIds().forEach(chainId -> {
//            StatisticInfo statistic = statisticService.getStatisticInfo(chainId);
//            BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),statistic);
//            messagingTemplate.convertAndSend("/topic/statistic/new?cid="+chainId, resp);
//        });
//    }
//
//    /**
//     * 推送区块相关信息
//     */
//    @Scheduled(cron="0/1 * * * * ?")
//    public void pushBlock(){
//        chainsConfig.getChainIds().forEach(chainId -> {
//            // 全量推送区块信息
//            List<BlockPushItem> blocks = blockCacheService.getBlockPushCache(chainId,1,10);
//            BaseResp blockResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blocks);
//            messagingTemplate.convertAndSend("/topic/block/new?cid="+chainId, blockResp);
//        });
//    }
//
//    /**
//     * 推送交易相关信息
//     */
//    @Scheduled(cron="0/1 * * * * ?")
//    public void pushTransaction(){
//        chainsConfig.getChainIds().forEach(chainId -> {
//            // 全量推送交易信息
//            List<TransactionPushItem> transactions = transactionCacheService.getTransactionPushCache(chainId,1,10);
//            BaseResp transactionResp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactions);
//            messagingTemplate.convertAndSend("/topic/transaction/new?cid="+chainId, transactionResp);
//        });
//    }
//}
