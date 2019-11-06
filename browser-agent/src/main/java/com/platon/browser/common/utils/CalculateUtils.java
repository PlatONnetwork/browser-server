package com.platon.browser.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import com.platon.browser.common.complement.bean.AnnualizedRateInfo;
import com.platon.browser.common.complement.bean.PeriodValueElement;

public class CalculateUtils {
	
	/**
	 * 当前增发周期开始块高
	 * @param addIssuePeriodBlockCount 每个增发周期块数
	 * @param curAddIssueEpoch 当前增发周期轮数
	 * @return
	 */
	public static Long calculateAddIssueBegin(BigInteger addIssuePeriodBlockCount, BigInteger curAddIssueEpoch) {
		return addIssuePeriodBlockCount.multiply(curAddIssueEpoch).subtract(addIssuePeriodBlockCount).longValue();
	}	
	
	/**
	 * 当前增发周期结束块高 
	 * @param addIssuePeriodBlockCount 每个增发周期块数
	 * @param curAddIssueEpoch 当前增发周期轮数
	 * @return
	 */
	public static Long calculateAddIssueEnd(BigInteger addIssuePeriodBlockCount, BigInteger curAddIssueEpoch) {
		return addIssuePeriodBlockCount.multiply(curAddIssueEpoch).longValue();
	}
	
	/**
	 * 离下个结算周期剩余块高
	 * @param settlePeriodBlockCount  每个结算周期区块总数
	 * @param curSettingEpoch 当前结算周期轮数
	 * @param curBlockNumber 当前块高
	 * @return
	 */
	public static Long calculateNextSetting(BigInteger settlePeriodBlockCount, BigInteger curSettingEpoch, BigInteger curBlockNumber) {
		return settlePeriodBlockCount.multiply(curSettingEpoch).subtract(curBlockNumber).longValue();
	}
	  
	/**
	 * 年化率计算
	 * @param ari 年化率信息
	 * @param settlePeriodCountPerIssue 一个增发周期包含结算周期的个数
	 * @return
	 */
    public static BigDecimal calculateAnnualizedRate(AnnualizedRateInfo ari, BigInteger settlePeriodCountPerIssue){
        // 年化率推算信息
        BigDecimal profitSum=BigDecimal.ZERO;
        BigDecimal costSum=BigDecimal.ZERO;
        
        // 利润=最新的收益累计-最旧的收益收益累计
        if(ari.getProfit().size()>0) {
            PeriodValueElement latest=ari.getProfit().get(0);
            PeriodValueElement oldest=latest;
            for (PeriodValueElement pve:ari.getProfit()){
                if(latest==null||latest.getPeriod().compareTo(pve.getPeriod())<0) latest=pve;
                if(oldest==null||latest.getPeriod().compareTo(pve.getPeriod())>0) oldest=pve;
            }
            if (latest==oldest) profitSum=latest.getValue();
            if (latest!=oldest) profitSum=latest.getValue().subtract(oldest.getValue());
        }

        // 按周期从小到大排序
        ari.getCost().sort((c1,c2)-> Integer.compare(c1.getPeriod().compareTo(c2.getPeriod()), 0));
        int count = 0;
        for (PeriodValueElement pve:ari.getCost()){
            if(count==4) break;
            	costSum= costSum.add(pve.getValue());
            count++;
        }
        
        if(costSum.compareTo(BigDecimal.ZERO)==0) {
            return BigDecimal.ZERO;
        }
        BigDecimal rate = profitSum
                .divide(costSum,16,RoundingMode.FLOOR) // 除总成本
                .multiply(new BigDecimal(settlePeriodCountPerIssue)) // 乘每个增发周期的结算周期数
                .multiply(BigDecimal.valueOf(100));
        return rate.setScale(2,RoundingMode.FLOOR);
    }
	
}
