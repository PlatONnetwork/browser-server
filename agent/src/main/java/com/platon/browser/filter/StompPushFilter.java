package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.dto.agent.StompPushDto;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomStatisticsMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 11:28
 */
@Component
public class StompPushFilter {

    private static Logger log = LoggerFactory.getLogger(StompPushFilter.class);

    @Value("${platon.redis.key.staticstics}")
    private String staticsticsCacheKeyTemplate;

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;

    @Value("${platon.redis.key.maxtps}")
    private String maxtpsCacheKeyTemplate;

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private CustomStatisticsMapper customStatisticsMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate2;

    public boolean StompPushFliter( Block block ,List<NodeRanking> nodeRankings){
        StompPushDto stompPushDto = new StompPushDto();
        String stompDtoCacheKey = staticsticsCacheKeyTemplate.replace("{}",chainId);
        Object stompDto = redisTemplate.opsForValue().get(stompDtoCacheKey);

        /************* 设置当前块高、出块节点*************/
        stompPushDto.setMiner(block.getMiner());
        stompPushDto.setCurrentHeight(block.getNumber());

        /************* 设置交易笔数***********/
        stompPushDto.setTransactionCount(block.getTransactionNumber());


        /************* 设置地址数*************/
        long addressCount = customStatisticsMapper.countAddress(chainId);
        stompPushDto.setAddressCount(addressCount);

        /************* 设置共识节点数*************/
        stompPushDto.setConsensusCount(nodeRankings.size());

        /************** 计算24小时内的交易数 ************/
        long dayTransactionCount = customStatisticsMapper.countTransactionIn24Hours(chainId);
        stompPushDto.setDayTransaction(dayTransactionCount);

        /************** 计算平均区块交易数 ************/
        BigDecimal avgBlockTrans = customStatisticsMapper.countAvgTransactionPerBlock(chainId);
        stompPushDto.setAvgTransaction(avgBlockTrans);

        /************** 计算平均出块时长 *************/
        String blockCacheKey = blockCacheKeyTemplate.replace("{}",chainId);
        Set<String> oldest = redisTemplate2.opsForZSet().reverseRange(blockCacheKey,3599,3599);
        Set<String> newest = redisTemplate2.opsForZSet().reverseRange(blockCacheKey,0,0);
        if(oldest.size()==0){
            // 总共不足3600个块，则正向取第一个
            oldest = redisTemplate2.opsForZSet().range(blockCacheKey,0,0);
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
        stompPushDto.setAvgTime(avgTime);

        /************** 计算最大TPS ************/
        String maxtpsCacheKey = maxtpsCacheKeyTemplate.replace("{}",chainId);
        String maxtpsStr = redisTemplate2.opsForValue().get(maxtpsCacheKey);
        long maxtpsLong = 0;
        if(StringUtils.isNotBlank(maxtpsStr)){
            maxtpsLong = Long.valueOf(maxtpsStr);
        }
        stompPushDto.setMaxTps(maxtpsLong);
        if(maxtpsLong<stompPushDto.getCurrent()){
            stompPushDto.setMaxTps(stompPushDto.getCurrent());
            redisTemplate2.opsForValue().set(maxtpsCacheKey,String.valueOf(stompPushDto.getMaxTps()));
        }


        /************** 计算当前TPS ************/
        Date endDate = new Date(block.getTimestamp().getTime());
        Date startDate = new Date(block.getTimestamp().getTime()-1000);

        BlockExample blockExample = new BlockExample();
        blockExample.createCriteria().andChainIdEqualTo(chainId).andTimestampBetween(startDate,endDate);
        List<Block> blockList=blockMapper.selectByExample(blockExample);

        stompPushDto.setCurrent(0);
        if(blockList.size()>0){
            blockList.forEach(blocks -> stompPushDto.setCurrent(stompPushDto.getCurrent()+block.getTransactionNumber()));
        }

        redisTemplate.opsForValue().set(stompDtoCacheKey,stompPushDto);
        return true;
    }
}