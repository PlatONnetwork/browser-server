package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    @Value("${platon.redis.block.cache.key}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.transaction.cache.key}")
    private String transactionCacheKeyTemplate;
    @Value("${platon.redis.cache.max_item_num}")
    private long maxItemNum;

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
            // 把交易所属区块号与交易索引号相加作为score
            Long value = (item.getBlockNumber()+item.getTransactionIndex());
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
        RespPage<BlockItem> page = new RespPage<>();

        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(chainId);
        Long totalCount = blockMapper.countByExample(blockExample);
        page.setTotalCount(totalCount.intValue());

        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        Long size = redisTemplate.opsForZSet().size(cacheKey);
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));
        Long pageCount = size/pageSize;
        if(size%pageSize!=0){
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
        List<BlockItem> blocks = new LinkedList<>();
        long serverTime = System.currentTimeMillis();
        cache.forEach(str -> {
            Block block = JSON.parseObject(str,Block.class);
            BlockItem bean = new BlockItem();
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getNumber());
            bean.setServerTime(serverTime);
            bean.setTimestamp(block.getTimestamp().getTime());
            bean.setTransaction(block.getTransactionNumber());
            blocks.add(bean);
        });
        page.setData(blocks);
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

        RespPage<TransactionItem> page = new RespPage<>();

        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId);
        Long totalCount = transactionMapper.countByExample(transactionExample);
        page.setTotalCount(totalCount.intValue());

        Long size = redisTemplate.opsForZSet().size(cacheKey);
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));
        Long pageCount = size/pageSize;
        if(size%pageSize!=0){
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
        List<TransactionItem> transactions = new LinkedList<>();
        long serverTime = System.currentTimeMillis();


        // 查询交易所属的区块信息
        List<Long> blockNumberList = new LinkedList<>();
        List<Transaction> cacheItems = new LinkedList<>();
        cache.forEach(str->{
            Transaction transaction = JSON.parseObject(str,Transaction.class);
            cacheItems.add(transaction);
            blockNumberList.add(transaction.getBlockNumber());
        });

        Map<Long, Block> map = new HashMap<>();
        if(blockNumberList.size()>0){
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainId)
                    .andNumberIn(blockNumberList);
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            blocks.forEach(block->map.put(block.getNumber(),block));
        }

        // 获取缓存中的交易信息
        cacheItems.forEach(transaction -> {
            TransactionItem bean = new TransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setBlockHeight(transaction.getBlockNumber());
            bean.setServerTime(serverTime);
            Block block = map.get(transaction.getBlockNumber());
            if(block!=null){
                bean.setBlockTime(block.getTimestamp().getTime());
            }
            transactions.add(bean);
        });
        page.setData(transactions);
        return page;
    }
}
