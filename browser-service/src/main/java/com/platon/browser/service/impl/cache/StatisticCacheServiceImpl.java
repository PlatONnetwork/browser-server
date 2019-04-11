package com.platon.browser.service.impl.cache;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.CustomStatisticsMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticPushItem;
import com.platon.browser.dto.StatisticsCache;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.service.cache.BlockCacheService;
import com.platon.browser.service.cache.NodeCacheService;
import com.platon.browser.service.cache.StatisticCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.TicketContract;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Component
public class StatisticCacheServiceImpl extends CacheBase implements StatisticCacheService {

    private final Logger logger = LoggerFactory.getLogger(StatisticCacheServiceImpl.class);

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.key.staticstics}")
    private String staticsticsCacheKeyTemplate;
    @Value("${platon.redis.key.trans-count}")
    private String transCountCacheKeyTemplate;
    @Value("${platon.redis.key.trans-count-24-hours}")
    private String transCount24HoursCacheKeyTemplate;
    @Value("${platon.redis.key.address-count}")
    private String addressCountCacheKeyTemplate;
    @Value("${platon.redis.key.avg-block-trans-count}")
    private String avgBlockTransCountCacheKeyTemplate;
    @Value("${platon.redis.key.ticket-price}")
    private String ticketPriceCacheKeyTemplate;
    @Value("${platon.redis.key.vote-count}")
    private String voteCountCacheKeyTemplate;
    @Value("${platon.redis.key.max-tps}")
    private String maxTpsCacheKeyTemplate;

    @Autowired
    private PlatonClient platon;
    @Autowired
    private CustomStatisticsMapper customStatisticsMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private BlockCacheService blockCacheService;
    @Autowired
    private NodeCacheService nodeCacheService;

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
            Block initData = JSON.parseObject(str, Block.class);
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
     * 清除统计缓存
     * @param chainId
     */
    @Override
    public void clearStatisticsCache(String chainId) {
        String cacheKey = staticsticsCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.delete(cacheKey);
    }

    /**
     * 更新统计缓存
     * @param chainId
     */
    @Override
    public boolean updateStatisticsCache(String chainId){
        long beginTime = System.currentTimeMillis();

        StatisticsCache cache = getStatisticsCache(chainId);
        if(cache==null) cache = new StatisticsCache();

        RespPage<BlockListItem> blockPage = blockCacheService.getBlockPage(chainId,1,1);
        List<BlockListItem> sampleBlocks = blockPage.getData();
        if(sampleBlocks.size()>0){
            BlockListItem cachedHighestBlock = sampleBlocks.get(0);
            if(cache.getCurrentHeight()>cachedHighestBlock.getHeight()){
                // 如果参数的块高比缓存中的块高小，则不作更新
                return false;
            }
            /************* 设置当前块高、出块节点、节点名称、节点ID*************/
            if(cache.getCurrentHeight()<cachedHighestBlock.getHeight()){
                cache.setMiner(cachedHighestBlock.getMiner());
                cache.setCurrentHeight(cachedHighestBlock.getHeight());
                cache.setNodeName(StringUtils.isBlank(cachedHighestBlock.getNodeName())?"GenesisNode":cachedHighestBlock.getNodeName());
                cache.setNodeId(cachedHighestBlock.getNodeId());
            }
            /************** 计算当前TPS: 因为链上每秒出一个块，所以最高块中的交易数就是TPS ************/
            cache.setCurrent(cachedHighestBlock.getTransaction());
        }
        logger.debug("  |-Time Consuming(updateStatisticsCache()[TPS]): {}ms",System.currentTimeMillis()-beginTime);

        /************** 计算最大TPS ************/
        if(cache.getMaxTps()<cache.getCurrent()){
            cache.setMaxTps(cache.getCurrent());
        }
        long fakeMaxTps = getMaxTps(chainId);
        if(fakeMaxTps>0){
            // 如果设置了假的MAX_TPS,则以假的为准
            cache.setMaxTps(fakeMaxTps);
        }

        List<NodePushItem> nodes = nodeCacheService.getNodePushCache(chainId);
        /************* 设置共识节点数*************/
        cache.setConsensusCount(nodes.size());


        long querySummaryBeginTime = System.currentTimeMillis();
        /************* 总交易数*************/
        cache.setTransactionCount(getTransCount(chainId));
        /************* 设置地址数*************/
        cache.setAddressCount(getAddressCount(chainId));
        /************** 计算24小时内的交易数 ************/
        cache.setDayTransaction(getTransCount24Hours(chainId));
        /************** 计算平均区块交易数 ************/
        cache.setAvgTransaction(getAvgBlockTransCount(chainId));
        logger.debug("  |-Time Consuming(updateStatisticsCache()[COUNT]): {}ms",System.currentTimeMillis()-querySummaryBeginTime);

        long avgBlockTimeBeginTime = System.currentTimeMillis();
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
            Block oldestBlock = JSON.parseObject(oldest.iterator().next(), Block.class);
            lowestBlockNumber=oldestBlock.getNumber();
            lowestBlockTimestamp=oldestBlock.getTimestamp().getTime();
        }
        if(newest.size()!=0){
            Block newestBlock = JSON.parseObject(newest.iterator().next(), Block.class);
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
        logger.debug("  |-Time Consuming(updateStatisticsCache()[AVG_BLOCK_TIME]): {}ms",System.currentTimeMillis()-avgBlockTimeBeginTime);


        long ticketContractBeginTime = System.currentTimeMillis();
        /*********************** 获取当前投票数、占比、票价 ***********************/
        cache.setTicketPrice(getTicketPrice(chainId));
        cache.setVoteCount(getVoteCount(chainId));
        // 占比
        BigDecimal proportion = BigDecimal.valueOf(cache.getVoteCount()).divide(BigDecimal.valueOf(51200),4, RoundingMode.DOWN);
        cache.setProportion(proportion);
        logger.debug("  |-Time Consuming(updateStatisticsCache()[TICKET_CONTRACT]): {}ms",System.currentTimeMillis()-ticketContractBeginTime);

        String cacheKey = staticsticsCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(cache));

        logger.debug("Time Consuming(Total): {}ms",System.currentTimeMillis()-beginTime);

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

    /**
     * 更新总交易笔数
     */
    @Override
    public boolean updateTransCount(String chainId) {
        /************* 设置总交易笔数***********/
        String cacheKey = transCountCacheKeyTemplate.replace("{}",chainId);
        TransactionExample transactionCon = new TransactionExample();
        transactionCon.createCriteria().andChainIdEqualTo(chainId);
        long totalTransaction = transactionMapper.countByExample(transactionCon);
        redisTemplate.opsForValue().set(cacheKey,String.valueOf(totalTransaction));
        return true;
    }

    @Override
    public long getTransCount(String chainId) {
        String cacheKey = transCountCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return Long.valueOf(res);
        return 0;
    }

    /**
     * 更新24小时内的交易笔数
     */
    @Override
    public boolean updateTransCount24Hours(String chainId) {
        /************** 计算24小时内的交易数 ************/
        String cacheKey = transCount24HoursCacheKeyTemplate.replace("{}",chainId);
        long dayTransactionCount = customStatisticsMapper.countTransactionIn24Hours(chainId);
        redisTemplate.opsForValue().set(cacheKey,String.valueOf(dayTransactionCount));
        return true;
    }

    @Override
    public long getTransCount24Hours(String chainId) {
        String cacheKey = transCount24HoursCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return Long.valueOf(res);
        return 0;
    }

    /**
     * 更新地址数
     */
    @Override
    public boolean updateAddressCount(String chainId) {
        /************* 设置地址数*************/
        String cacheKey = addressCountCacheKeyTemplate.replace("{}",chainId);
        long addressCount = customStatisticsMapper.countAddress(chainId);
        redisTemplate.opsForValue().set(cacheKey,String.valueOf(addressCount));
        return true;
    }

    @Override
    public long getAddressCount(String chainId) {
        String cacheKey = addressCountCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return Long.valueOf(res);
        return 0;
    }

    /**
     * 更新区块平均交易数
     */
    @Override
    public boolean updateAvgBlockTransCount(String chainId) {
        /************** 计算平均区块交易数 ************/
        String cacheKey = avgBlockTransCountCacheKeyTemplate.replace("{}",chainId);
        BigDecimal avgBlockTrans = customStatisticsMapper.countAvgTransactionPerBlock(chainId);
        if(avgBlockTrans==null) avgBlockTrans = BigDecimal.ZERO;
        redisTemplate.opsForValue().set(cacheKey,String.valueOf(avgBlockTrans));
        return true;
    }

    @Override
    public BigDecimal getAvgBlockTransCount(String chainId) {
        String cacheKey = avgBlockTransCountCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return new BigDecimal(res);
        return BigDecimal.ZERO;
    }

    /**
     * 更新票价
     * @param chainId
     * @return
     */
    @Override
    public boolean updateTicketPrice(String chainId) {
        String cacheKey = ticketPriceCacheKeyTemplate.replace("{}",chainId);
        /*********************** 获取当前投票数、占比、票价 ***********************/
        TicketContract ticketContract = platon.getTicketContract(chainId);
        // 票价
        try {
            String ticketPrice = ticketContract.GetTicketPrice().send();
            if(StringUtils.isNotBlank(ticketPrice)){
                redisTemplate.opsForValue().set(cacheKey,Convert.fromWei(ticketPrice, Convert.Unit.ETHER).toString());
            }else{
                redisTemplate.opsForValue().set(cacheKey,"0");
            }
        } catch (Exception e) {
            redisTemplate.opsForValue().set(cacheKey,"0");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public BigDecimal getTicketPrice(String chainId) {
        String cacheKey = ticketPriceCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return new BigDecimal(res);
        return BigDecimal.ZERO;
    }

    /**
     * 更新投票数
     * @param chainId
     * @return
     */
    @Override
    public boolean updateVoteCount(String chainId) {
        String cacheKey = voteCountCacheKeyTemplate.replace("{}",chainId);
        // 投票数
        TicketContract ticketContract = platon.getTicketContract(chainId);
        try {
            String remain = ticketContract.GetPoolRemainder().send();
            if(StringUtils.isNotBlank(remain)){
                redisTemplate.opsForValue().set(cacheKey,String.valueOf(51200-Long.valueOf(remain)));
            }else{
                redisTemplate.opsForValue().set(cacheKey,"0");
            }
        } catch (Exception e) {
            redisTemplate.opsForValue().set(cacheKey,"0");
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public long getVoteCount(String chainId) {
        String cacheKey = voteCountCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return Long.valueOf(res);
        return 0;
    }

    /**
     * 设置最高TPS
     * @param chainId
     * @param value
     */
    @Override
    public boolean updateMaxTps(String chainId, String value) {
        if(StringUtils.isBlank(value)) return false;
        String cacheKey = maxTpsCacheKeyTemplate.replace("{}",chainId);
        redisTemplate.opsForValue().set(cacheKey,value);
        return true;
    }

    @Override
    public long getMaxTps(String chainId) {
        String cacheKey = maxTpsCacheKeyTemplate.replace("{}",chainId);
        String res = redisTemplate.opsForValue().get(cacheKey);
        if(StringUtils.isNotBlank(res)) return Long.valueOf(res);
        return 0;
    }
}
