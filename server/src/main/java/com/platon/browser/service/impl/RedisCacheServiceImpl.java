package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.fartherp.framework.common.util.IPUtils;
import com.maxmind.geoip.Location;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.*;
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
import javax.print.DocFlavor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Pattern;


@Component
public class RedisCacheServiceImpl implements RedisCacheService {

    private final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    @Value("${platon.redis.block.cache.key}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.transaction.cache.key}")
    private String transactionCacheKeyTemplate;
    @Value("${platon.redis.cache.max_item_num}")
    private long maxItemNum;

    @Value("${platon.redis.node.cache.key}")
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
        RespPage<BlockItem> page = new RespPage<>();

        String cacheKey = blockCacheKeyTemplate.replace("{}",chainId);

        Long size = redisTemplate.opsForZSet().size(cacheKey);

        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));

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

        // 设置总记录大小
        cache = redisTemplate.opsForZSet().reverseRange(cacheKey,0,0);
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

        RespPage<TransactionItem> page = new RespPage<>();

        page.setDisplayTotalCount(transactionCountMap.get(chainId));

        Long size = redisTemplate.opsForZSet().size(cacheKey);
        page.setTotalCount(size.intValue());
        page.setErrMsg(i18n.i(I18nEnum.SUCCESS));


        Long pagingTotalCount = size;
        if(pagingTotalCount>maxItemNum){
            // 如果缓存数量大于maxItemNum，则以maxItemNum作为分页数量
            pagingTotalCount = maxItemNum;
        }
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
        List<TransactionItem> transactions = new LinkedList<>();
        long serverTime = System.currentTimeMillis();


        // 把缓存中的字符串转换为交易实体
        List<Transaction> cacheItems = new LinkedList<>();
        cache.forEach(str->{
            Transaction transaction = JSON.parseObject(str,Transaction.class);
            cacheItems.add(transaction);
        });

        // 获取缓存中的交易信息
        cacheItems.forEach(transaction -> {
            TransactionItem bean = new TransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setBlockHeight(transaction.getBlockNumber());
            bean.setServerTime(serverTime);
            // 交易时间就是出块时间
            bean.setBlockTime(transaction.getTimestamp().getTime());
            transactions.add(bean);
        });
        page.setData(transactions);
        return page;
    }

    @Override
    public List<NodeInfo> getNodeList(String chainId) {
        List<NodeInfo> nodeInfoList = new LinkedList<>();

        String cacheKey = nodeCacheKeyTemplate.replace("{}",chainId);
        String cacheData = redisTemplate.opsForValue().get(cacheKey);

        if(StringUtils.isBlank(cacheData)){
            return nodeInfoList;
        }

        List<Node> nodeList = JSON.parseArray(cacheData,Node.class);
        nodeList.forEach(node -> {
            NodeInfo bean = new NodeInfo();
            BeanUtils.copyProperties(node,bean);

            String ip = node.getIp();
            if(!IPUtil.isIPv4Address(ip)){
                try {
                    ip = InetAddress.getByName(ip).getHostAddress();
                } catch (UnknownHostException e) {
                    ip = "";
                    e.printStackTrace();
                }
            }


            Location location = GeoUtil.getLocation(ip);
            if(location!=null){
                bean.setLongitude(location.longitude);
                bean.setLatitude(location.latitude);
            }else{
                // 默认设置为深圳的经纬度
                bean.setLongitude(114.06667f);
                bean.setLatitude(22.61667f);
            }

            bean.setNodeType(1);
            bean.setNetState(node.getNodeStatus());
            nodeInfoList.add(bean);
        });
        return nodeInfoList;
    }


}
