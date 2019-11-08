package com.platon.browser.common.utils;

import com.platon.browser.common.complement.dto.AnnualizedRateInfo;
import com.platon.browser.common.complement.dto.PeriodValueElement;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.CustomStaking;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;

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

	public static BigDecimal calculationIssueValue( BigInteger issueEpoch, BlockChainConfig chainConfig, BigInteger incentivePoolAccountBalance){
		Map <Integer, BigDecimal> subsidiesMap = chainConfig.getFoundationSubsidies();
		int curIssueEpoch=issueEpoch.intValue();
		int subsidiesSize=subsidiesMap.size();

		// 基金会补贴部分
		BigDecimal foundationAmount = BigDecimal.ZERO;
		// 前subsidiesSize年才有补贴，其余时候为0
		if(curIssueEpoch<=subsidiesSize) foundationAmount = subsidiesMap.get(curIssueEpoch);

		//获取初始发行金额
		BigDecimal initIssueAmount = chainConfig.getInitIssueAmount();
		initIssueAmount = Convert.toVon(initIssueAmount, Convert.Unit.LAT);
		//获取增发比例
		BigDecimal addIssueRate = chainConfig.getAddIssueRate();

		//年份增发量 = (1+增发比例)的增发年份次方
		BigDecimal circulationByYear = BigDecimal.ONE.add(addIssueRate).pow(curIssueEpoch);
		//计算发行量 = 初始发行量 * 年份增发量 - 实时激励池余额 + 第N年基金会补贴
		// 发行量
		BigDecimal issueValue = initIssueAmount
				.multiply(circulationByYear)
				.subtract(new BigDecimal(incentivePoolAccountBalance))
				.add(foundationAmount);
		return issueValue;
	}


	public static BigDecimal calculationTurnValue(BigDecimal issueValue,BigInteger inciteBalance,BigInteger stakingBalance,BigInteger restrictBalance){
		BigDecimal turnValue = issueValue
				.subtract(new BigDecimal(restrictBalance))
				.subtract(new BigDecimal(stakingBalance))
				.subtract(new BigDecimal(inciteBalance));

		return turnValue;
	}


	/**
	 * 记录利润
	 * @param staking
	 */
	public static void rotateProfit( Staking staking,BigInteger curSettingEpoch, AnnualizedRateInfo ari,BlockChainConfig blockChainConfig)  {
/*		if(ari==null){
			throw new SettleEpochChangeException("年化率信息为空，无法计算!");
		}*/
		// 如果年化率推算信息不为空，则证明当前质押信息已经连续了几个结算周期，做以下操作：
		// 添加上一周期的收益

		BigDecimal profit = staking.getStakingRewardValue().add(staking.getBlockRewardValue());
		ari.getProfit().add(new PeriodValueElement(curSettingEpoch.longValue(),profit));
		if(ari.getProfit().size()>blockChainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()){
			// 按结算周期由大到小排序
			ari.getProfit().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
			// 删除多余的元素
			for (int i=ari.getProfit().size()-1;i>=blockChainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue();i--) ari.getProfit().remove(i);
		}
	}

}
