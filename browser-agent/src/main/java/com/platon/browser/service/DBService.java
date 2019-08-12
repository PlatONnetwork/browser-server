//package com.platon.browser.service;
//
//import com.platon.browser.client.PlatonClient;
//import com.platon.browser.dao.mapper.BlockMapper;
//import com.platon.browser.dao.mapper.TransactionMapper;
//import com.platon.browser.service.cache.BlockCacheService;
//import com.platon.browser.service.cache.NodeCacheService;
//import com.platon.browser.service.cache.StatisticCacheService;
//import com.platon.browser.service.cache.TransactionCacheService;
//import com.platon.browser.thread.AnalyseThread;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//@Component
//public class DBService {/*
//    private static Logger logger = LoggerFactory.getLogger(DBService.class);
//    @Autowired
//    private PlatonClient platon;
//    @Value("${platon.chain.active}")
//    private String chainId;
//    @Autowired
//    private BlockMapper blockMapper;
//    @Autowired
//    private TransactionMapper transactionMapper;
//    @Autowired
//    private BlockCacheService blockCacheService;
//    @Autowired
//    private NodeCacheService nodeCacheService;
//    @Autowired
//    private TransactionCacheService transactionCacheService;
//    @Autowired
//    private StatisticCacheService statisticCacheService;
//
//    @Value("${platon.redis.key.address-trans-key-template}")
//    private String tranListPrefix;
//    @Autowired
//    protected RedisTemplate <String,String> redisTemplate;
//
//    @Transactional
//    public void flush(AnalyseThread.AnalyseResult result){
//        long beginTime = System.currentTimeMillis();
//        if(result.blocks.size()>0){
//            long blockInertBeginTime = beginTime;
//            blockMapper.batchInsert(result.blocks);
//            logger.debug("  |-Time Consuming(blockMapper.batchInsert()): {}ms",System.currentTimeMillis()-blockInertBeginTime);
//            long updateBlockCacheBeginTime = System.currentTimeMillis();
//            blockCacheService.updateBlockCache(chainId, new HashSet<>(result.blocks));
//            logger.debug("  |-Time Consuming(redisCacheService.updateBlockCache): {}ms",System.currentTimeMillis()-updateBlockCacheBeginTime);
//            Map<String,Long> nodeIdMaxBlockNumMap = new HashMap<>();
//            result.blocks.forEach(block->{
//                Long num = nodeIdMaxBlockNumMap.get(block.getNodeId());
//                // 如果num为空或num小于当前块号
//                if(num==null || (num!=null&&num<block.getNumber())) nodeIdMaxBlockNumMap.put(block.getNodeId(),block.getNumber());
//            });
//            if(nodeIdMaxBlockNumMap.size()>0) nodeCacheService.updateNodeIdMaxBlockNum(chainId,nodeIdMaxBlockNumMap);
//        }

//        if(result.errorBlocks.size()>0) {
//            // 先删后插，防止重复主键导致插入失败，以及防止因为重复主键错误导致整个事务回滚
//            List<Long> numbers = new ArrayList<>();
//            result.errorBlocks.forEach(err->numbers.add(err.getNumber()));
//            BlockMissingExample example = new BlockMissingExample();
//            example.createCriteria().andChainIdEqualTo(chainId).andNumberIn(numbers);
//            blockMissingMapper.deleteByExample(example);
//            blockMissingMapper.batchInsert(result.errorBlocks);
//        }
//
//        long updateStatisticsCache = System.currentTimeMillis();
//        statisticCacheService.updateStatisticsCache(chainId);
//        logger.debug("  |-Time Consuming(redisCacheService.updateStatisticsCache()): {}ms",System.currentTimeMillis()-updateStatisticsCache);
//
//        logger.debug("Time Consuming(Total): {}ms",System.currentTimeMillis()-beginTime);
//    }
//
//}

