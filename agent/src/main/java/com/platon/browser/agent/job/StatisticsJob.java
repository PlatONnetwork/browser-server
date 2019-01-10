package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Statistics;
import com.platon.browser.dao.entity.StatisticsExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.StatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: dongqile
 * Date: 2018/12/5
 * Time: 17:16
 * 统计任务
 */
public class StatisticsJob extends AbstractTaskJob {


    private static Logger logger = LoggerFactory.getLogger(StatisticsJob.class);

    private static final long offset = 100000L;

    @Value("${chain.id}")
    private String chainId;


    @Autowired
    private BlockMapper blockMapper;

  /*  @Autowired
    private NodeMapper nodeMapper;*/

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private RedisTemplate <String, Object> redisTemplate;

    @Value("${platon.redis.number.cache.key}")
    private String statisticNumber;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            //get statistic number in redis
            String cacheKey = statisticNumber.replace("{}",chainId);
            Object numberObject = redisTemplate.opsForValue().get(cacheKey);
            long number = 0;
            if(null != numberObject){
                number = Long.valueOf(String.valueOf(numberObject)).longValue();
            }

            logger.debug("Redis number : [",number,"]");
            //get NodeList
            Map <String, Double> nodeRewardMap = new HashMap <>();
/*            NodeExample nodeExample = new NodeExample();
            nodeExample.createCriteria().andChainIdEqualTo(chainId);
            List <Node> nodeList = nodeMapper.selectByExample(nodeExample);
            //get node reward
            for (Node node : nodeList) {
                nodeRewardMap.put(node.getAddress(), node.getRewardRatio());
            }*/
            //build four type map statistic info (key(miner) -> value(typeValue))
            Map <String, String> blockCountMap = new HashMap <>();
            Map <String, String> blockRewardMap = new HashMap <>();
            Map <String, String> rewardAmountMap = new HashMap <>();
            Map <String, String> profitAmountMap = new HashMap <>();

            //get blockList (from:startoffset to:endoffset)
            long startOffset = 0, endOffset = offset;
            if (number != 0) {
                startOffset = number;
                endOffset = number + offset;
            }

            List <Block> blocks = new ArrayList <>();
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainId)
                    .andNumberGreaterThanOrEqualTo(startOffset)
                    .andNumberLessThanOrEqualTo(endOffset);
            blocks = blockMapper.selectByExample(blockExample);

            //blockinfo is null ,return
            if(blocks.size() < 0){
                logger.error("block info is null!...");
                return;
            }
            //statistic block info put Corresponding map
            for (Block block : blocks) {
                Long count = 0L;
                count = count + 1;
                //blockCount
                blockCountMap.put(block.getMiner(), String.valueOf(count));

                BigDecimal sum = new BigDecimal("0");
                sum = sum.add(new BigDecimal(block.getBlockReward()));
                //blockReward
                blockRewardMap.put(block.getMiner(), sum.toString());

                BigDecimal dividend = new BigDecimal("0");
                Double rewardRatio = nodeRewardMap.get(block.getMiner());
                //nodeReward
                //get blockRewar ,if blockReward is null ,set rewardRatio is 1
                if (StringUtils.isEmpty(rewardRatio)) {
                    rewardRatio = 1D;
                }
                dividend = dividend.add(new BigDecimal(block.getActualTxCostSum())).multiply(new BigDecimal(String.valueOf(1 - rewardRatio)));
                rewardAmountMap.put(block.getMiner(), dividend.toString());

                //profitAmount
                BigDecimal cumulative = new BigDecimal("0");
                cumulative = cumulative.add(new BigDecimal(block.getBlockReward()).multiply(new BigDecimal(String.valueOf(rewardRatio))));
                profitAmountMap.put(block.getMiner(), cumulative.toString());
                number = number +1;
            }
            //map key put address List
            Set <String> blockCountMinerList = blockCountMap.keySet();
            List <String> addressList = new ArrayList <>();
            if(addressList.size() <= 0){
                long newOffset =  number;
                setRedisTemplate(cacheKey, newOffset);
                logger.error("addressList info is null!...");
                return;
            }
            addressList.addAll(blockCountMinerList);
            //condition is addressList and chain ,select table statistic info
            StatisticsExample condition = new StatisticsExample();
            condition.createCriteria().andChainIdEqualTo(chainId)
                    .andAddressIn(addressList);
            List <Statistics> statisticsList = statisticsMapper.selectByExample(condition);
            if(statisticsList.size() <= 0){
                //select result is null ,return and set number
                long newOffset =  number;
                setRedisTemplate(cacheKey, newOffset);
                logger.error("statistic info is null!...");
                return;
            }
            for (Statistics statistics : statisticsList) {
                //if no null ,
                switch (StatisticsEnum.valueOf(statistics.getType())) {
                    case profit_amount:
                        String newProfitAmount = profitAmountMap.get(statistics.getAddress());
                        BigDecimal A = new BigDecimal(statistics.getValue());
                        BigDecimal B = new BigDecimal(newProfitAmount);
                        statistics.setValue(A.add(B).toString());
                        break;
                    case block_reward:
                        String newReward = blockRewardMap.get(statistics.getAddress());
                        BigDecimal C = new BigDecimal(statistics.getValue());
                        BigDecimal D = new BigDecimal(newReward);
                        statistics.setValue(C.add(D).toString());
                        break;
                    case block_count:
                        String newCount = blockCountMap.get(statistics.getAddress());
                        BigDecimal E = new BigDecimal(statistics.getValue());
                        BigDecimal F = new BigDecimal(newCount);
                        statistics.setValue(E.add(F).toString());
                        break;
                    case reward_amount:
                        String newRewardAmount = rewardAmountMap.get(statistics.getAddress());
                        BigDecimal G = new BigDecimal(statistics.getValue());
                        BigDecimal H = new BigDecimal(newRewardAmount);
                        statistics.setValue(G.add(H).toString());
                        break;
                }
            }
            for (Statistics statistics : statisticsList) {
                //update stockValue
                statisticsMapper.updateByPrimaryKey(statistics);
                //TODO: 查看，记得删除
                String  statisticsString  = JSON.toJSONString(statistics);

                //update redis number value
                logger.debug("statistic info :{ " , statisticsString ," }");
                long newOffset =  number;
                setRedisTemplate(cacheKey, newOffset);
                logger.debug("StaticticsJob : [ nodeInfo statistic succ!... ]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("StaticticsJob Exception!...", e.getMessage());
        } finally {
            stopWatch.stop();
        }
    }

    private void setRedisTemplate(String cacheKey,long newOffset){
        try {
            redisTemplate.opsForValue().set(cacheKey, newOffset);
            logger.debug("Redis number : [",newOffset,"]");
            logger.debug("StaticticdJob set redisValue succ!");
        }catch (Exception e){
            logger.error("StaticticdJob set redisValue fail!",e.getMessage());
        }
    }
}