package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.dto.statistic.NetworkStatChange;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;

public class StatisticsNetworkConverter {
	
	@Autowired
	private NetworkStatCache networkStatCache;
    @Autowired
    private NodeCache nodeCache;
	@Autowired
	private AccountService accountService;
    @Autowired
    private BlockChainConfig chainConfig;
    
	
    public NetworkStatChange convert(CollectionEvent event,CollectionBlock block, EpochMessage epochMessage) throws Exception {
    	
        //获取激励池余额
		BigInteger inciteBalance = accountService.getInciteBalance(BigInteger.valueOf(block.getNum()));
		//获取质押余额
		BigInteger stakingBalance = accountService.getStakingBalance(BigInteger.valueOf(block.getNum()));
		//获取锁仓余额
		BigInteger restrictBalance = accountService.getLockCabinBalance(BigInteger.valueOf(block.getNum()));
		//计算发行量
		BigDecimal issueValue = CalculateUtils.calculationIssueValue(epochMessage.getIssueEpochRound(),chainConfig,inciteBalance);
		//计算流通量
		BigDecimal turnValue = CalculateUtils.calculationTurnValue(issueValue,inciteBalance,stakingBalance,restrictBalance);
		// 网络统计
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        NetworkStatChange networkStatChange = NetworkStatChange.builder()
        	.id(1)
        	.curNumber(block.getNum())
        	.nodeId(block.getNodeId())
        	.nodeName(nodeCache.getNode(block.getNodeId()).getNodeName())
        	.txQty(networkStat.getTxQty())
        	.curTps(networkStat.getCurTps())
        	.maxTps(networkStat.getMaxTps())
        	.proposalQty(networkStat.getProposalQty())
        	.blockReward(new BigDecimal(epochMessage.getBlockReward()))
        	.stakingReward(new BigDecimal(epochMessage.getStakeReward()))
        	.addIssueBegin(CalculateUtils.calculateAddIssueBegin(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
        	.addIssueEnd(CalculateUtils.calculateAddIssueEnd(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
        	.nextSettle(CalculateUtils.calculateNextSetting(chainConfig.getSettlePeriodBlockCount(), epochMessage.getSettleEpochRound(), epochMessage.getCurrentBlockNumber()))
			.issueValue(issueValue) // 发行量
			.turnValue(turnValue) // 流通量
        	.build();
    	
        return networkStatChange;
    }
}
