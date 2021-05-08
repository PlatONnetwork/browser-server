package com.platon.browser.task;

import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.service.account.AccountService;
import com.platon.browser.utils.AppStatusUtil;
import com.platon.browser.utils.CalculateUtils;
import com.platon.browser.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.bean.CustomNode;
import com.platon.browser.task.bean.NetworkStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: 网络统计相关信息更新任务
 */

@Component
@Slf4j
public class NetworkStatUpdateTask {
	
	@Resource
	private NetworkStatCache networkStatCache;
    @Resource
    private BlockChainConfig chainConfig;
	@Resource
	private AccountService accountService;
	@Resource
	private StatisticBusinessMapper statisticBusinessMapper;
	@Resource
	private NodeMapper nodeMapper;
	
    @Scheduled(cron = "0/1  * * * * ?")
    public void networkStatUpdate() {
		// 只有程序正常运行才执行任务
		if(AppStatusUtil.isRunning()) start();
    }
	protected void start (){
		try {
			NetworkStat networkStat = networkStatCache.getNetworkStat();
			Long curNumber = networkStat.getCurNumber();
			//config中获取增发年份
			BigDecimal issueEpochRound = chainConfig.getIssueEpochRound();
			//获取激励池余额
			BigDecimal inciteBalance = accountService.getInciteBalance(BigInteger.valueOf(curNumber));
			//获取质押余额
			BigDecimal stakingBalance = accountService.getStakingBalance(BigInteger.valueOf(curNumber));
			//获取锁仓余额
			BigDecimal restrictBalance = accountService.getLockCabinBalance(BigInteger.valueOf(curNumber));
			//获取锁仓余额
			BigDecimal rewardBalance = accountService.getRewardBalance(BigInteger.valueOf(curNumber));
			//计算发行量
			BigDecimal issueValue = CalculateUtils.calculationIssueValue(new BigInteger(issueEpochRound.toString()),chainConfig,inciteBalance,networkStat.getIssueRates());
			//计算流通量
			BigDecimal turnValue = CalculateUtils.calculationTurnValue(chainConfig,networkStat.getIssueRates(),inciteBalance,stakingBalance,restrictBalance,rewardBalance);
			//计算可使用质押量
			BigDecimal availableStaking = CalculateUtils.calculationAvailableValue(networkStat.getIssueRates(),chainConfig,inciteBalance);
			//获得节点相关的网络统计
			NetworkStatistics networkStatistics = statisticBusinessMapper.getNetworkStatisticsFromNode();
			BigDecimal totalValue = networkStatistics.getTotalValue() == null ? BigDecimal.ZERO : networkStatistics.getTotalValue();
			BigDecimal stakingValue = networkStatistics.getStakingValue() == null ? BigDecimal.ZERO : networkStatistics.getStakingValue();
			//获得地址数统计
			int addressQty = statisticBusinessMapper.getNetworkStatisticsFromAddress();
			// 重新计算质押奖励
			NodeExample nodeExample = new NodeExample();
			nodeExample.createCriteria().andIsSettleEqualTo(CustomNode.YesNoEnum.YES.getCode());
			long nodeCount = nodeMapper.countByExample(nodeExample);
			BigDecimal stakingReward=networkStatCache.getNetworkStat().getStakingReward();
			if(nodeCount!=0){
				stakingReward=networkStatCache.getNetworkStat().getSettleStakingReward().divide(BigDecimal.valueOf(nodeCount),16, RoundingMode.FLOOR);
			}

			//获得进行中的提案
			int doingProposalQty = statisticBusinessMapper.getNetworkStatisticsFromProposal();
			networkStatCache.updateByTask(issueValue,turnValue,availableStaking,totalValue,stakingValue,addressQty,doingProposalQty,stakingReward);
		} catch (Exception e) {
			log.error("网络统计任务出错:",e);
		}
	}
}
