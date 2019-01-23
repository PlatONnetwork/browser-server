package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.dto.StatisticsCache;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomStatisticsMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticPushItem;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Component
public class RedisCacheServiceImpl implements RedisCacheService {

    private final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    @Value("${platon.redis.key.max-item}")
    private long maxItemNum;
    @Value("${platon.redis.key.node}")
    private String nodeCacheKeyTemplate;
    @Value("${platon.redis.key.staticstics}")
    private String staticsticsCacheKeyTemplate;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private CustomStatisticsMapper customStatisticsMapper;

    @Autowired
    private I18nUtil i18n;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private boolean validateParam(String chainId,Collection items){
        if (!chainsConfig.getChainIds().contains(chainId)){
            // 非法链ID
            logger.error("Invalid Chain ID: {}", chainId);
            return false;
        }
        if(items.size()==0){
            // 无更新内容
            logger.error("Empty Content");
            return false;
        }
        return true;
    }

    private static class CachePageInfo<T>{
        Set<String> data;
        RespPage<T> page;
    }

    private <T> CachePageInfo getCachePageInfo(String cacheKey,int pageNum,int pageSize,T target){
        RespPage<T> page = new RespPage<>();
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));

        CachePageInfo<T> cpi = new CachePageInfo<>();
        Long size = redisTemplate.opsForZSet().size(cacheKey);
        Long pagingTotalCount = size;
        if(pagingTotalCount>maxItemNum){
            // 如果缓存数量大于maxItemNum，则以maxItemNum作为分页数量
            pagingTotalCount = maxItemNum;
        }
        page.setTotalCount(pagingTotalCount.intValue());

        Long pageCount = pagingTotalCount/pageSize;
        if(pagingTotalCount%pageSize!=0){
            pageCount+=1;
        }
        page.setTotalPages(pageCount.intValue());

        // Redis的缓存分页从索引0开始
        if(pageNum<=0){
            pageNum=1;
        }
        if(pageSize<=0){
            pageSize=1;
        }
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        cpi.data = cache;
        cpi.page = page;
        return cpi;
    }

    /**
     * 清除区块缓存
     * @param chainId
     */
    @Override
    public void clearBlockCache(String chainId) {
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 更新区块缓存
     * @param chainId
     */
    @Override
    public void updateBlockCache(String chainId, Set<Block> items){
        if(!validateParam(chainId,items))return;
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        long size = redisTemplate.opsForZSet().size(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(block -> {
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,block.getNumber(),block.getNumber());
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(block),block.getNumber().doubleValue()));
            }
        });
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }
        if(size>maxItemNum){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
            redisTemplate.opsForZSet().removeRange(cacheKey,0,size-maxItemNum);
        }
    }

    /**
     * 重置区块缓存
     * @param chainId
     */
    @Override
    public void resetBlockCache(String chainId, boolean clearOld) {
        if(clearOld) clearBlockCache(chainId);
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        for(int i=0;i<1000;i++){
            PageHelper.startPage(i+1,500);
            List<Block> data = blockMapper.selectByExample(condition);
            if(data.size()==0) break;
            updateBlockCache(chainId,new HashSet<>(data));
        }
    }

    /**
     * 清除交易缓存
     * @param chainId
     */
    @Override
    public void clearTransactionCache(String chainId) {
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 更新交易缓存
     * @param chainId
     */
    @Override
    public void updateTransactionCache(String chainId, Set<Transaction> items){
        if(!validateParam(chainId,items))return;
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        long size = redisTemplate.opsForZSet().size(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(transaction -> {
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,transaction.getSequence(),transaction.getSequence());
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(transaction),transaction.getSequence().doubleValue()));
            }
        });
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }
        if(size>maxItemNum){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
            redisTemplate.opsForZSet().removeRange(cacheKey,0,size-maxItemNum);
        }
    }

    /**
     * 重置交易缓存
     * @param chainId
     */
    @Override
    public void resetTransactionCache(String chainId,boolean clearOld) {
        if(clearOld) clearTransactionCache(chainId);
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("block_number desc,transaction_index desc");
        for(int i=0;i<500;i++){
            PageHelper.startPage(i+1,1000);
            List<Transaction> data = transactionMapper.selectByExample(condition);
            if(data.size()==0) break;
            updateTransactionCache(chainId,new HashSet<>(data));
        }
    }

    /**
     * 根据页数和每页大小获取区块的缓存分页
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public RespPage<BlockListItem> getBlockPage(String chainId, int pageNum, int pageSize){
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        CachePageInfo<BlockListItem> cpi = getCachePageInfo(cacheKey,pageNum,pageSize, BlockListItem.class);
        RespPage<BlockListItem> page = cpi.page;
        List<BlockListItem> blocks = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cpi.data.forEach(str -> {
            Block initData = JSON.parseObject(str,Block.class);
            BlockListItem bean = new BlockListItem();
            bean.init(initData);
            bean.setServerTime(serverTime);
            blocks.add(bean);
        });
        page.setData(blocks);
        // 设置总记录大小
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,0,0);
        if(cache.size()>0){
            Block block = JSON.parseObject(cache.iterator().next(),Block.class);
            page.setDisplayTotalCount(block.getNumber()==null?0:block.getNumber().intValue());
        }else{
            page.setDisplayTotalCount(0);
        }
        return page;
    }

    /**
     * 获取区块推送数据
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<BlockPushItem> getBlockPushCache(String chainId, int pageNum, int pageSize){
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        List<BlockPushItem> returnData = new LinkedList<>();
        cache.forEach(str -> {
            Block initData = JSON.parseObject(str,Block.class);
            BlockPushItem bean = new BlockPushItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }

    /**
     * 获取统计推送数据
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<StatisticPushItem> getStatisticPushCache(String chainId, int pageNum, int pageSize){
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        List<StatisticPushItem> returnData = new LinkedList<>();
        cache.forEach(str -> {
            Block initData = JSON.parseObject(str,Block.class);
            StatisticPushItem bean = new StatisticPushItem();
            bean.init(initData);
            returnData.add(bean);
        });

        Collections.sort(returnData,(c1, c2)->{
            // 按区块高度正排
            if(c1.getHeight()>c2.getHeight()) return 1;
            if(c1.getHeight()<c2.getHeight()) return -1;
            return 0;
        });

        return returnData;
    }

    /**
     * 根据页数和每页大小获取交易的缓存分页
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public RespPage<TransactionListItem> getTransactionPage(String chainId, int pageNum, int pageSize){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        CachePageInfo<TransactionListItem> cpi = getCachePageInfo(cacheKey,pageNum,pageSize, TransactionListItem.class);
        RespPage<TransactionListItem> page = cpi.page;
        List<TransactionListItem> transactions = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cpi.data.forEach(str->{
            TransactionListItem bean = new TransactionListItem();
            Transaction transaction = JSON.parseObject(str,Transaction.class);
            bean.init(transaction);
            bean.setServerTime(serverTime);
            transactions.add(bean);
        });
        page.setData(transactions);
        // 设置总记录大小
        Long displayCount = transactionMapper.countByExample(new TransactionExample());
        page.setDisplayTotalCount(displayCount.intValue());
        return page;
    }

    /**
     * 获取区块推送数据
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<TransactionPushItem> getTransactionPushCache(String chainId, int pageNum, int pageSize){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(cacheKey,(pageNum-1)*pageSize,(pageNum*pageSize)-1);
        List<TransactionPushItem> returnData = new LinkedList<>();
        cache.forEach(str -> {
            Transaction initData = JSON.parseObject(str,Transaction.class);
            TransactionPushItem bean = new TransactionPushItem();
            bean.init(initData);
            returnData.add(bean);
        });
        return returnData;
    }

    /**
     * 清除节点推送缓存
     * @param chainId
     */
    @Override
    public void clearNodePushCache(String chainId) {
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 更新节点推送缓存
     * @param chainId
     */
    @Override
    public void updateNodePushCache(String chainId, Set<NodeRanking> items) {
        if(!validateParam(chainId,items))return;
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
        List<String> nodes = new ArrayList<>();
        items.forEach(initData -> {
            NodePushItem bean = new NodePushItem();
            bean.init(initData);
            nodes.add(JSON.toJSONString(bean));
        });
        if(nodes.size()>0){
            redisTemplate.opsForList().leftPushAll(cacheKey,nodes);
        }
    }

    /**
     * 重置节点推送缓存
     * @param chainId
     */
    @Override
    public void resetNodePushCache(String chainId, boolean clearOld) {
        clearNodePushCache(chainId);
        NodeRankingExample condition = new NodeRankingExample();
        condition.createCriteria().andChainIdEqualTo(chainId)
                .andIsValidEqualTo(1);
        List<NodeRanking> data = nodeRankingMapper.selectByExample(condition);
        if(data.size()==0) return;
        updateNodePushCache(chainId,new HashSet<>(data));
    }

    /**
     * 获取节点推送缓存
     * @param chainId
     */
    @Override
    public List<NodePushItem> getNodePushCache(String chainId) {
        List<NodePushItem> returnData = new LinkedList<>();
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        List<String> cacheData = redisTemplate.opsForList().range(cacheKey,0,-1);
        if(cacheData.size()==0){
            return returnData;
        }
        cacheData.forEach(nodeStr -> {
            NodePushItem bean = JSON.parseObject(nodeStr,NodePushItem.class);
            returnData.add(bean);
        });
        Collections.sort(returnData,(n1,n2)->{
            if(n1.getRanking()>n2.getRanking()) return 1;
            if(n1.getRanking()<n2.getRanking()) return -1;
            return 0;
        });
        return returnData;
    }



    private final static ReadWriteLock LOCK = new ReentrantReadWriteLock();
    /**
     * 更新统计缓存
     * @param chainId
     */
    @Override
    public boolean updateStatisticsCache( String chainId, Block block , List<NodeRanking> nodeRankings){

        StatisticsCache cache = getStatisticsCache(chainId);
        if(cache==null) cache = new StatisticsCache();

        if(cache.getCurrentHeight()>block.getNumber()){
            // 如果参数的块高比缓存中的块高小，则不作更新
            return false;
        }

        /************* 设置总交易笔数***********/
        TransactionExample transactionCon = new TransactionExample();
        transactionCon.createCriteria().andChainIdEqualTo(chainId);
        long totalTransaction = transactionMapper.countByExample(transactionCon);
        cache.setTransactionCount(totalTransaction);


        /************* 设置地址数*************/
        long addressCount = customStatisticsMapper.countAddress(chainId);
        cache.setAddressCount(addressCount);

        /************* 设置共识节点数*************/
        cache.setConsensusCount(nodeRankings.size());

        /************** 计算24小时内的交易数 ************/
        long dayTransactionCount = customStatisticsMapper.countTransactionIn24Hours(chainId);
        cache.setDayTransaction(dayTransactionCount);

        /************** 计算平均区块交易数 ************/
        BigDecimal avgBlockTrans = customStatisticsMapper.countAvgTransactionPerBlock(chainId);
        cache.setAvgTransaction(avgBlockTrans);

        /************* 设置当前块高、出块节点、节点名称、节点ID*************/
        if(cache.getCurrentHeight()<block.getNumber()){
            cache.setMiner(block.getMiner());
            cache.setCurrentHeight(block.getNumber());
            cache.setNodeName(StringUtils.isBlank(block.getNodeName())?"Unknown":block.getNodeName());
            cache.setNodeId(block.getNodeId());
        }

        /************** 计算平均出块时长 *************/
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
        cache.setAvgTime(avgTime);

        /************** 计算当前TPS ************/
        Date endDate = new Date(block.getTimestamp().getTime());
        Date startDate = new Date(block.getTimestamp().getTime()-1000);
        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(chainId).andTimestampBetween(startDate,endDate);
        List<Block> blocks = blockMapper.selectByExample(blockExample);
        cache.setCurrent(0);
        if(blocks.size()>0){
            for (Block e : blocks) cache.setCurrent(cache.getCurrent()+e.getTransactionNumber());
        }


        /************** 计算最大TPS ************/
        if(cache.getMaxTps()<cache.getCurrent()){
            cache.setMaxTps(cache.getCurrent());
        }

        String cacheKey = staticsticsCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(cache));

        return true;
    }

    /**
     * 获取统计缓存
     * @param chainId
     */
    @Override
    public StatisticsCache getStatisticsCache(String chainId){
        String cacheKey = staticsticsCacheKeyTemplate.replace("{}",chainId);
        String cacheStr = redisTemplate.opsForValue().get(cacheKey);
        StatisticsCache cache = null;
        if(StringUtils.isNotBlank(cacheStr)){
            cache = JSON.parseObject(cacheStr,StatisticsCache.class);
        }
        if(cache==null){
            return new StatisticsCache();
        }
        return cache;
    }

}
