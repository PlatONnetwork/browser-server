package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.GeoUtil;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import com.platon.browser.util.IPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private Map<String,Integer> transactionCountMap = new HashMap<>();

    @PostConstruct
    private void init(){
        chainsConfig.getChainIds().forEach(chainId->{
            TransactionExample transactionExample = new TransactionExample();
            transactionExample.createCriteria().andChainIdEqualTo(chainId);
            Long count = transactionMapper.countByExample(transactionExample);
            transactionCountMap.put(chainId,count.intValue());
        });
    }

    public void updateTransactionCount(String chainId, int step){
        transactionCountMap.put(chainId,transactionCountMap.get(chainId)+step);
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
     * 更新区块缓存
     * @param chainId
     */
    @Override
    public void updateBlockCache(String chainId, Set<Block> items){
        if (!chainsConfig.getChainIds().contains(chainId)){
            // 非法链ID
            logger.error("Invalid Chain ID: {}", chainId);
            return;
        }
        if(items.size()==0){
            // 无更新内容
            logger.error("Empty Content");
            return;
        }
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        long size = redisTemplate.opsForZSet().size(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(item -> {
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,item.getNumber(),item.getNumber());
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(item),item.getNumber().doubleValue()));
            }
        });
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }

        if(size>maxItemNum){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
            long count = redisTemplate.opsForZSet().removeRange(cacheKey,0,size-maxItemNum);
        }
    }

    /**
     * 更新交易缓存
     * @param chainId
     */
    @Override
    public void updateTransactionCache(String chainId, Set<Transaction> items){
        if (!chainsConfig.getChainIds().contains(chainId)){
            // 非法链ID
            logger.error("Invalid Chain ID: {}", chainId);
            return;
        }
        if(items.size()==0){
            // 无更新内容
            logger.error("Empty Content");
            return;
        }
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        long size = redisTemplate.opsForZSet().size(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(item -> {
            // 将数据库生成的sequence值作为score
            Long value = item.getSequence();
            // 根据score来判断缓存中的记录是否已经存在
            Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,value,value);
            if(exist.size()==0){
                // 在缓存中不存在的才放入缓存
                tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(item),value.doubleValue()));
            }
        });
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }

        if(size>maxItemNum){
            // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
            long count = redisTemplate.opsForZSet().removeRange(cacheKey,0,size-maxItemNum);
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
    public RespPage<BlockItem> getBlockPage(String chainId,int pageNum,int pageSize){
        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        CachePageInfo<BlockItem> cpi = getCachePageInfo(cacheKey,pageNum,pageSize,BlockItem.class);
        RespPage<BlockItem> page = cpi.page;
        List<BlockItem> blocks = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cpi.data.forEach(str -> {
            Block initData = JSON.parseObject(str,Block.class);
            BlockItem bean = new BlockItem();
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
     * 根据页数和每页大小获取交易的缓存分页
     * @param chainId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public RespPage<TransactionItem> getTransactionPage(String chainId, int pageNum, int pageSize){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        CachePageInfo<TransactionItem> cpi = getCachePageInfo(cacheKey,pageNum,pageSize,TransactionItem.class);
        RespPage<TransactionItem> page = cpi.page;
        List<TransactionItem> transactions = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cpi.data.forEach(str->{
            TransactionItem bean = new TransactionItem();
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

    @Override
    public void updateNodeCache(String chainId, Set<NodeRanking> items) {
        if (!chainsConfig.getChainIds().contains(chainId)){
            // 非法链ID
            logger.error("Invalid Chain ID: {}", chainId);
            return;
        }
        if(items.size()==0){
            // 无更新内容
            logger.error("Empty Content");
            return;
        }
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
        Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
        items.forEach(item -> tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(item),item.getRanking().doubleValue())));
        if(tupleSet.size()>0){
            redisTemplate.opsForZSet().add(cacheKey, tupleSet);
        }
    }

    @Override
    public List<NodeInfo> getNodeList(String chainId) {
        List<NodeInfo> nodeInfoList = new LinkedList<>();
        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        Set<String> cacheData = redisTemplate.opsForZSet().reverseRange(cacheKey,0,-1);
        if(cacheData.size()==0){
            return nodeInfoList;
        }
        cacheData.forEach(nodeStr -> {
            NodeRanking initData = JSON.parseObject(nodeStr,NodeRanking.class);
            NodeInfo bean = new NodeInfo();
            bean.init(initData);
            nodeInfoList.add(bean);
        });
        return nodeInfoList;
    }

}
