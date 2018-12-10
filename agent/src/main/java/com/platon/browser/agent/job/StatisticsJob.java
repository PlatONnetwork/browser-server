package com.platon.browser.agent.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.StaticticsDto;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
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

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private RedisTemplate <String, String> redisTemplate;

    @Autowired
    private CustomBlockMapper customBlockMapper;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            ConfigConst.loadConfigPath();
            String number = redisTemplate.opsForValue().get(ConfigConst.getChainId());
            logger.debug("Redis number : [",number,"]");
            Map <String, Double> nodeRewardMap = new HashMap <>();
            NodeExample nodeExample = new NodeExample();
            nodeExample.createCriteria().andChainIdEqualTo(ConfigConst.getChainId());
            List <Node> nodeList = nodeMapper.selectByExample(nodeExample);
            for (Node node : nodeList) {
                nodeRewardMap.put(node.getAddress(), node.getRewardRatio());
            }
            Map <String, String> blockCountMap = new HashMap <>();
            Map <String, String> blockRewardMap = new HashMap <>();
            Map <String, String> rewardAmountMap = new HashMap <>();
            Map <String, String> profitAmountMap = new HashMap <>();
            List <Block> blocks = new ArrayList <>();
            if (number == null) {
                //if on redis number is null,that mean first init
                blocks = customBlockMapper.selectBlockByNumber(ConfigConst.getChainId(), 0L, 0L + offset);
            } else {
                //else that mean initialized ,than get offset
                blocks = customBlockMapper.selectBlockByNumber(ConfigConst.getChainId(), Long.valueOf(number), Long.valueOf(number) + offset);
            }
            if(blocks.size() < 0){
                logger.error("block info is null!...");
                return;
            }
            for (Block block : blocks) {
                Long count = 0L;
                count = count + 1;
                blockCountMap.put(block.getMiner(), String.valueOf(count));

                BigInteger sum = new BigInteger("0");
                sum = sum.add(new BigInteger(block.getBlockReward()));
                blockRewardMap.put(block.getMiner(), sum.toString());

                BigInteger dividend = new BigInteger("0");
                Double rewardRatio = nodeRewardMap.get(block.getMiner());
                if (rewardRatio.equals(null)) {
                    rewardRatio = 1D;
                }
                dividend = dividend.add(new BigInteger(block.getActualTxCostSum())).multiply(new BigInteger(String.valueOf(1 - rewardRatio)));
                rewardAmountMap.put(block.getMiner(), dividend.toString());

                BigInteger cumulative = new BigInteger("0");
                cumulative = cumulative.add(new BigInteger(block.getBlockReward()).multiply(new BigInteger(String.valueOf(rewardRatio))));
                profitAmountMap.put(block.getMiner(), cumulative.toString());
            }
            Set <String> blockCountMinerList = blockCountMap.keySet();
            List <String> addressList = new ArrayList <>();
            addressList.addAll(blockCountMinerList);

            StatisticsExample condition = new StatisticsExample();
            condition.createCriteria().andChainIdEqualTo(ConfigConst.getChainId())
                    .andAddressIn(addressList);
            List <Statistics> statisticsList = statisticsMapper.selectByExample(condition);
            if(statisticsList.size() < 0){
                logger.error("statistic info is null!...");
                return;
            }
            for (Statistics statistics : statisticsList) {
                switch (StatisticsEnum.valueOf(statistics.getType())) {
                    case profit_amount:
                        String newProfitAmount = profitAmountMap.get(statistics.getAddress());
                        BigInteger A = new BigInteger(statistics.getValue());
                        BigInteger B = new BigInteger(newProfitAmount);
                        statistics.setValue(A.add(B).toString());
                        break;
                    case block_reward:
                        String newReward = blockRewardMap.get(statistics.getAddress());
                        BigInteger C = new BigInteger(statistics.getValue());
                        BigInteger D = new BigInteger(newReward);
                        statistics.setValue(C.add(D).toString());
                        break;
                    case block_count:
                        String newCount = blockCountMap.get(statistics.getAddress());
                        BigInteger E = new BigInteger(statistics.getValue());
                        BigInteger F = new BigInteger(newCount);
                        statistics.setValue(E.add(F).toString());
                        break;
                    case reward_amount:
                        String newRewardAmount = rewardAmountMap.get(statistics.getAddress());
                        BigInteger G = new BigInteger(statistics.getValue());
                        BigInteger H = new BigInteger(newRewardAmount);
                        statistics.setValue(G.add(H).toString());
                        break;
                }
            }
            for (Statistics statistics : statisticsList) {
                statisticsMapper.updateByPrimaryKey(statistics);
                //TODO: 查看，记得删除
                String  statisticsString  = JSON.toJSONString(statistics);
                logger.debug("statistic info :{ " , statisticsString ," }");
            }
            String newOffset = String.valueOf(Long.valueOf(number) + offset);
            logger.debug("Redis number : [",newOffset,"]");
            redisTemplate.opsForValue().set(ConfigConst.getChainId(), newOffset);
            logger.debug("StaticticsJob : [ nodeInfo statistic succ!... ]");
        } catch (Exception e) {
            logger.error("StaticticsJob Exception!...", e.getMessage());
        } finally {
            stopWatch.stop();
        }
    }

}