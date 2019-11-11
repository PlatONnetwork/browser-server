package com.platon.browser.task;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.platon.browser.common.utils.AppStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.service.account.AccountService;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.complement.dao.entity.NetworkStatistics;
import com.platon.browser.complement.dao.mapper.StatisticBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: TODO: 网络统计相关信息更新任务
 */

@Component
@Slf4j
public class NetworkStatUpdateTask {
	
	@Autowired
	private NetworkStatCache networkStatCache;
    @Autowired
    private BlockChainConfig chainConfig;
	@Autowired
	private AccountService accountService;
	@Autowired
	private StatisticBusinessMapper statisticBusinessMapper;
	
    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
		// 只有程序正常运行才执行任务
		if(AppStatusUtil.isRunning()) start();
    }
	protected void start () throws InterruptedException {
		try {
			Long curNumber = networkStatCache.getNetworkStat().getCurNumber();

			BigInteger issueEpochRound = EpochUtil.getEpoch(BigInteger.valueOf(curNumber),chainConfig.getAddIssuePeriodBlockCount());
			//获取激励池余额
			BigInteger inciteBalance = accountService.getInciteBalance(BigInteger.valueOf(curNumber));
			//获取质押余额
			BigInteger stakingBalance = accountService.getStakingBalance(BigInteger.valueOf(curNumber));
			//获取锁仓余额
			BigInteger restrictBalance = accountService.getLockCabinBalance(BigInteger.valueOf(curNumber));
			//计算发行量
			BigDecimal issueValue = CalculateUtils.calculationIssueValue(issueEpochRound,chainConfig,inciteBalance);
			//计算流通量
			BigDecimal turnValue = CalculateUtils.calculationTurnValue(issueValue,inciteBalance,stakingBalance,restrictBalance);
			//获得节点相关的网络统计
			NetworkStatistics networkStatistics = statisticBusinessMapper.getNetworkStatisticsFromNode();
			BigDecimal totalValue = networkStatistics.getTotalValue() == null ? BigDecimal.ZERO : networkStatistics.getTotalValue();
			BigDecimal stakingValue = networkStatistics.getStakingValue() == null ? BigDecimal.ZERO : networkStatistics.getStakingValue();
			//获得地址数统计
			int addressQty = statisticBusinessMapper.getNetworkStatisticsFromAddress();
			//获得进行中的提案
			int doingProposalQty = statisticBusinessMapper.getNetworkStatisticsFromProposal();

			networkStatCache.updateByTask(issueValue,turnValue,totalValue,stakingValue,addressQty,doingProposalQty);

		} catch (BlockNumberException e) {
			log.error("on NetworkStatUpdateTask error",e);
		}
	}
}
