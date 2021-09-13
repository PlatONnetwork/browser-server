package com.platon.browser.analyzer.statistic;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.EpochMessage;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.custommapper.StatisticBusinessMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.utils.CalculateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class StatisticsNetworkAnalyzer {
	
	@Resource
	private NetworkStatCache networkStatCache;
    @Resource
    private NodeCache nodeCache;
    @Resource
    private BlockChainConfig chainConfig;
    @Resource
    private StatisticBusinessMapper statisticBusinessMapper;
    
	
    public void analyze(CollectionEvent event, Block block, EpochMessage epochMessage) throws NoSuchBeanException {
        long startTime = System.currentTimeMillis();
        log.debug("block({}),transactions({}),consensus({}),settlement({}),issue({})",block.getNum(),event.getTransactions().size(),epochMessage.getConsensusEpochRound(),epochMessage.getSettleEpochRound(),epochMessage.getIssueEpochRound());
		// 网络统计
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        networkStat.setNodeId(block.getNodeId());
        networkStat.setNodeName(nodeCache.getNode(block.getNodeId()).getNodeName());
        networkStat.setNextSettle(CalculateUtils.calculateNextSetting(chainConfig.getSettlePeriodBlockCount(), epochMessage.getSettleEpochRound(), epochMessage.getCurrentBlockNumber()));
        networkStat.setNodeOptSeq(networkStat.getNodeOptSeq());
        statisticBusinessMapper.networkChange(networkStat);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}
