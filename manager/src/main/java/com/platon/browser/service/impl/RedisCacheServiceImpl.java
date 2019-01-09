package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;


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

    @Autowired
    private I18nUtil i18n;
    @Autowired
    private TransactionMapper transactionMapper;
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


    private <T> void updateCache(String cacheKey,Collection<T> items){
        long size = redisTemplate.opsForZSet().size(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(item -> {
            Long startOffset=0l,endOffset=0l,score=0l;
            if(item instanceof Block){
                Block bl = (Block)item;
                startOffset = endOffset = score = bl.getNumber();
            }
            if(item instanceof Transaction){
                Transaction tr = (Transaction)item;
                startOffset = endOffset = score = tr.getSequence();
            }
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,startOffset,endOffset);
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(item),score.doubleValue()));
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
     * 更新区块缓存
     * @param chainId
     */
    @Override
    public void updateBlockCache(String chainId, Set<Block> items){
        if(!validateParam(chainId,items))return;
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        updateCache(cacheKey,items);
    }

    /**
     * 更新交易缓存
     * @param chainId
     */
    @Override
    public void updateTransactionCache(String chainId, Set<Transaction> items){
        if(!validateParam(chainId,items))return;
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        updateCache(cacheKey,items);
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
    public List<BlockPushItem> getBlockPushData(String chainId, int pageNum, int pageSize){
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
    public List<StatisticPushItem> getStatisticPushData(String chainId, int pageNum, int pageSize){
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
    public List<TransactionPushItem> getTransactionPushData(String chainId, int pageNum, int pageSize){
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

    @Override
    public void updateNodeCache(String chainId, Set<NodeRanking> items) {
        if(!validateParam(chainId,items))return;
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(item -> tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(item),item.getRanking().doubleValue())));
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }
    }

    @Override
    public List<NodePushItem> getNodeList(String chainId) {
        List<NodePushItem> nodeInfoList = new LinkedList<>();
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        Set<String> cacheData = redisTemplate.opsForZSet().reverseRange(cacheKey,0,-1);
        if(cacheData.size()==0){
            return nodeInfoList;
        }
        cacheData.forEach(nodeStr -> {
            NodeRanking initData = JSON.parseObject(nodeStr,NodeRanking.class);
            NodePushItem bean = new NodePushItem();
            bean.init(initData);
            nodeInfoList.add(bean);
        });
        return nodeInfoList;
    }
}
