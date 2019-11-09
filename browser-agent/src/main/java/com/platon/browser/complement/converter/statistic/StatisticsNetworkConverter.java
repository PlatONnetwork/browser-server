package com.platon.browser.complement.converter.statistic;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.complement.dao.param.statistic.NetworkStatChange;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Service
public class StatisticsNetworkConverter {
	
	@Autowired
	private NetworkStatCache networkStatCache;
    @Autowired
    private NodeCache nodeCache;
	@Autowired
	private AccountService accountService;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private StatisticBusinessMapper statisticBusinessMapper;
    
	
    public void convert(CollectionEvent event, Block block, EpochMessage epochMessage) throws Exception {
        long startTime = System.currentTimeMillis();

		// 网络统计
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        NetworkStatChange networkStatChange = NetworkStatChange.builder().build();
        BeanUtils.copyProperties(networkStat,networkStatChange);
        // 设置需要根据当前上下文更新的数据字段
        networkStatChange.setCurNumber(block.getNum())
                .setNodeId(block.getNodeId())
                .setNodeName(nodeCache.getNode(block.getNodeId()).getNodeName())
                .setBlockReward(new BigDecimal(epochMessage.getBlockReward()))
                .setStakingReward(new BigDecimal(epochMessage.getStakeReward()))
                .setAddIssueBegin(CalculateUtils.calculateAddIssueBegin(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
                .setAddIssueEnd(CalculateUtils.calculateAddIssueEnd(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
                .setNextSettle(CalculateUtils.calculateNextSetting(chainConfig.getSettlePeriodBlockCount(), epochMessage.getSettleEpochRound(), epochMessage.getCurrentBlockNumber()))
                .setIssueValue(BigDecimal.ZERO) // TODO: 发行量 需要在入库时合并RPC定时任务缓存中对应的值
                .setTurnValue(BigDecimal.ZERO) // TODO: 流通量 需要在入库时合并RPC定时任务缓存中对应的值
                .setNodeOptSeq(networkStat.getNodeOptSeq());
        statisticBusinessMapper.networkChange(networkStatChange);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}
