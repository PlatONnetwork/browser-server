package com.platon.browser.agent.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.platon.browser.common.constant.ConfigConst;
import com.platon.browser.common.dto.agent.StaticticsDto;
import com.platon.browser.common.enums.StatisticsEnum;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.CustomStaticticeMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StatisticsMapper;
import jnr.ffi.annotations.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import sun.security.krb5.Config;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/12/5
 * Time: 17:16
 * 统计任务
 */
public class StatisticsJob extends AbstractTaskJob {


    private static Logger logger = LoggerFactory.getLogger(StatisticsJob.class);

    @Autowired
    private NodeMapper nodeMapper;

    @Autowired
    private BlockMapper blockMapper;

    @Autowired
    private CustomStaticticeMapper customStaticticeMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            List <Statistics> list = new ArrayList <>();
            List <StaticticsDto> nodeList = customStaticticeMapper.selectNodeInfo(ConfigConst.getChainId());
            if (nodeList.size() > 0 && null != nodeList) {
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(ConfigConst.getChainId());
                List <Block> blockList = blockMapper.selectByExample(blockExample);
                if (blockList.size() > 0 && null != blockList) {
                    list = bulidInfo(nodeList, blockList);

                    statisticsMapper.batchInsert(list);
                }
                logger.error("StaicticsJob blockInfo is null!...");
            }
            logger.error("StaticticsJob nodeInfo is null!...");
        } catch (Exception e) {
            logger.error("StaticticsJob Exception!...", e.getMessage());
        } finally {
            stopWatch.stop();
        }
    }


    private List <Statistics> bulidInfo ( List <StaticticsDto> nodeList, List <Block> blockList ) {
        List <Statistics> list = new ArrayList <>();
        for (StaticticsDto staticticsDto : nodeList) {
            int count = 0;
            Statistics blockCount = new Statistics();
            blockCount.setChainId(ConfigConst.getChainId());
            blockCount.setNodeId(staticticsDto.getId());
            blockCount.setCreateTime(new Date());
            blockCount.setUpdateTime(new Date());
            blockCount.setType(StatisticsEnum.block_count.name());

            BigInteger sum = new BigInteger("0");
            Statistics blockReward = new Statistics();
            blockReward.setChainId(ConfigConst.getChainId());
            blockReward.setNodeId(staticticsDto.getId());
            blockReward.setCreateTime(new Date());
            blockReward.setUpdateTime(new Date());
            blockReward.setType(StatisticsEnum.block_reward.name());

            BigInteger dividend = new BigInteger("0");
            Statistics rewardAmount = new Statistics();
            rewardAmount.setChainId(ConfigConst.getChainId());
            rewardAmount.setNodeId(staticticsDto.getId());
            rewardAmount.setCreateTime(new Date());
            rewardAmount.setUpdateTime(new Date());
            rewardAmount.setType(StatisticsEnum.reward_amount.name());

            BigInteger cumulative = new BigInteger("0");
            Statistics profitAmount = new Statistics();
            profitAmount.setChainId(ConfigConst.getChainId());
            profitAmount.setNodeId(staticticsDto.getId());
            profitAmount.setCreateTime(new Date());
            profitAmount.setUpdateTime(new Date());
            profitAmount.setType(StatisticsEnum.profit_amount.name());
            for (Block block : blockList) {
                if (block.getMiner().equals(staticticsDto.getAddress())) {
                    count = count + 1;
                    sum = sum.add(new BigInteger(block.getBlockReward()));
                    dividend = dividend.add(new BigInteger(block.getActualTxCostSum())).multiply(new BigInteger(String.valueOf(1 - staticticsDto.getRewardRatio())));
                    cumulative = cumulative.add(new BigInteger(block.getBlockReward()).multiply(new BigInteger(String.valueOf(staticticsDto.getRewardRatio()))));
                }
            }
            blockCount.setValue(String.valueOf(count));
            blockReward.setValue(String.valueOf(sum));
            rewardAmount.setValue(String.valueOf(dividend));
            profitAmount.setValue(String.valueOf(cumulative));
            list.add(blockCount);
            list.add(blockReward);
            list.add(rewardAmount);
            list.add(profitAmount);
        }
        return list;
    }

}