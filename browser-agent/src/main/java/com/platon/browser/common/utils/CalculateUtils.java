package com.platon.browser.common.utils;

import com.platon.browser.common.complement.dto.AnnualizedRateInfo;
import com.platon.browser.common.complement.dto.PeriodValueElement;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Staking;
import lombok.extern.slf4j.Slf4j;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;

@Slf4j
public class CalculateUtils {

	private CalculateUtils(){}

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
	 * @param chainConfig 链配置
	 * @return
	 */
    public static BigDecimal calculateAnnualizedRate(AnnualizedRateInfo ari, BlockChainConfig chainConfig){
        // 年化率推算信息
        BigDecimal profitSum=BigDecimal.ZERO;
        BigDecimal costSum=BigDecimal.ZERO;
        
        // 利润=最新的收益累计-最旧的收益收益累计
		long profitMaxPeriod=0;
        if(!ari.getProfit().isEmpty()) {
            PeriodValueElement latest=ari.getProfit().get(0);
            PeriodValueElement oldest=latest;
            for (PeriodValueElement pve:ari.getProfit()){
                if(latest==null||latest.getPeriod().compareTo(pve.getPeriod())<0) latest=pve;
                if(oldest==null||latest.getPeriod().compareTo(pve.getPeriod())>0) oldest=pve;
                if(pve.getPeriod()>profitMaxPeriod) profitMaxPeriod=pve.getPeriod();
            }
            if (latest==oldest) profitSum=latest.getValue();
            if (latest!=oldest) profitSum=latest.getValue().subtract(oldest.getValue());
        }

		for (PeriodValueElement cost : ari.getCost()) {
			// 凡是小于或等于收益记录中最大周期的成本都累加起来
			if (cost.getPeriod() <= profitMaxPeriod) {
				costSum= costSum.add(cost.getValue());
			}
		}
        
        if(costSum.compareTo(BigDecimal.ZERO)==0) {
            return BigDecimal.ZERO;
        }
        BigDecimal rate = profitSum
                .divide(costSum,16,RoundingMode.FLOOR) // 除总成本
                .multiply(new BigDecimal(chainConfig.getSettlePeriodCountPerIssue())) // 乘每个增发周期的结算周期数
                .multiply(BigDecimal.valueOf(100));
        return rate.setScale(2,RoundingMode.FLOOR);
    }

	public static BigDecimal calculationIssueValue( BigInteger issueEpoch, BlockChainConfig chainConfig, BigDecimal incentivePoolAccountBalance){
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
		return initIssueAmount
				.multiply(circulationByYear)
				.subtract(incentivePoolAccountBalance)
				.add(foundationAmount != null?Convert.toVon(foundationAmount,Convert.Unit.LAT):BigDecimal.ZERO);
	}

	public static BigDecimal calculationTurnValue(BlockChainConfig chainConfig, BigInteger issueEpoch,BigDecimal inciteBalance,BigDecimal stakingBalance,BigDecimal restrictBalance){
		//当前块高所属结算周期
    	int curIssueEpoch=issueEpoch.intValue();
    	//获取初始发行金额
		BigDecimal initIssueAmount = chainConfig.getInitIssueAmount();
		initIssueAmount = Convert.toVon(initIssueAmount, Convert.Unit.LAT);
		//获取增发比例
		BigDecimal addIssueRate = chainConfig.getAddIssueRate();
		//年份增发量 = (1+增发比例)的增发年份次方
		BigDecimal circulationByYear = BigDecimal.ONE.add(addIssueRate).pow(curIssueEpoch);
		//计算流通量 = 初始发行量 * 年份增发量 - 锁仓余额  - 质押余额 - 实时激励池余额
    	return initIssueAmount.multiply(circulationByYear).subtract(restrictBalance)
				.subtract(stakingBalance)
				.subtract(inciteBalance);
	}

	/**
	 * 轮换记录利润
	 * @param staking
	 */
	public static void rotateProfit( Staking staking,BigInteger curSettingEpoch, AnnualizedRateInfo ari,BlockChainConfig chainConfig)  {
		// 添加上一周期的收益
		BigDecimal profit = staking.getStakingRewardValue().add(staking.getBlockRewardValue()).add(staking.getFeeRewardValue());
		if(curSettingEpoch.longValue()==0) profit=BigDecimal.ZERO;
		PeriodValueElement pve = new PeriodValueElement();
		pve.setPeriod(curSettingEpoch.longValue());
		pve.setValue(profit);
		ari.getProfit().add(pve);
		// +1: 保留指定周期数中最旧周期的前一周期收益，用作收益计算参考点
		if(ari.getProfit().size()>chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1){
			// 按结算周期由大到小排序
			ari.getProfit().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
			// 删除多余的元素, +1:保留指定周期数中最旧周期的前一周期收益，用作收益计算参考点
			for (int i=ari.getProfit().size()-1;i>=chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1;i--) ari.getProfit().remove(i);
		}
	}

	/**
	 * 轮换记录成本
	 * @param staking
	 * @param curSettingEpoch
	 * @param ari
	 * @param chainConfig
	 */
	public static void rotateCost(Staking staking,BigInteger curSettingEpoch,AnnualizedRateInfo ari,BlockChainConfig chainConfig) {
		// 添加下一周期的质押成本
		//
		BigDecimal cost = staking.getStakingLocked() // 锁定的质押金
				.add(staking.getStakingHes()) // 犹豫期的质押金
				.add(staking.getStatDelegateHes()) // 犹豫期的委托金
				.add(staking.getStatDelegateLocked()); // 锁定的委托金
		if(curSettingEpoch.longValue()==0) cost=BigDecimal.ZERO;
		PeriodValueElement pve = new PeriodValueElement();
		pve.setPeriod(curSettingEpoch.longValue());
		pve.setValue(cost);
		ari.getCost().add(pve);
		// 保留指定数量最新的记录
		if(ari.getCost().size()>chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1){
			// 按结算周期由大到小排序
			ari.getCost().sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
			// 删除多余的元素
			for (int i=ari.getCost().size()-1;i>=chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue()+1;i--) ari.getCost().remove(i);
		}
	}

}
