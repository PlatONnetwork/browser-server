package com.platon.browser.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: dongqile
 * @Date: 2019/11/6
 * @Description: TODO: 网络统计相关信息更新任务
 */

@Component
@Slf4j
public class NetworkStatUpdateTask {


    @Scheduled(cron = "0/30  * * * * ?")
    private void cron () throws InterruptedException {
//        //获取激励池余额
//		BigInteger inciteBalance = accountService.getInciteBalance(BigInteger.valueOf(block.getNum()));
//		//获取质押余额
//		BigInteger stakingBalance = accountService.getStakingBalance(BigInteger.valueOf(block.getNum()));
//		//获取锁仓余额
//		BigInteger restrictBalance = accountService.getLockCabinBalance(BigInteger.valueOf(block.getNum()));
//		//计算发行量
//		BigDecimal issueValue = CalculateUtils.calculationIssueValue(epochMessage.getIssueEpochRound(),chainConfig,inciteBalance);
//		//计算流通量
//		BigDecimal turnValue = CalculateUtils.calculationTurnValue(issueValue,inciteBalance,stakingBalance,restrictBalance);

    }

}
