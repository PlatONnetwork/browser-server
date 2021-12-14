package com.platon.browser.utils;

import com.platon.browser.bean.PeriodValueElement;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.constant.Browser;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.utils.Convert;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class CalculateUtils {

    private CalculateUtils() {
    }

    /**
     * 离下个结算周期剩余块高
     *
     * @param settlePeriodBlockCount 每个结算周期区块总数
     * @param curSettingEpoch        当前结算周期轮数
     * @param curBlockNumber         当前块高
     * @return
     */
    public static Long calculateNextSetting(BigInteger settlePeriodBlockCount, BigInteger curSettingEpoch, BigInteger curBlockNumber) {
        return settlePeriodBlockCount.multiply(curSettingEpoch).subtract(curBlockNumber).longValue();
    }

    /**
     * 计算可使用质押量
     *
     * @param networkStat:
     * @param incentivePoolAccountBalance:
     * @return: java.math.BigDecimal
     * @date: 2021/12/14
     */
    public static BigDecimal calculationAvailableValue(NetworkStat networkStat, BigDecimal incentivePoolAccountBalance) {
        return networkStat.getIssueValue().subtract(incentivePoolAccountBalance);
    }

    /**
     * 轮换利润
     *
     * @param profits         利润列表
     * @param curSettleProfit 当前结算周期的业务【质押/委托】利润
     * @param curSettleEpoch  当前结算周期轮数
     * @param chainConfig     链配置
     */
    public static void rotateProfit(List<PeriodValueElement> profits, BigDecimal curSettleProfit, BigInteger curSettleEpoch, BlockChainConfig chainConfig) {
        // 添加上一周期的收益
        if (curSettleEpoch.longValue() == 0) curSettleProfit = BigDecimal.ZERO; // 如果当前结算周期是0则利润为0
        PeriodValueElement pve = new PeriodValueElement();
        pve.setPeriod(curSettleEpoch.longValue());
        pve.setValue(curSettleProfit);
        profits.add(pve);
        // +1: 保留指定周期数中最旧周期的前一周期收益，用作收益计算参考点
        if (profits.size() > chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue() + 1) {
            // 按结算周期由大到小排序
            profits.sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
            // 删除多余的元素, +1:保留指定周期数中最旧周期的前一周期收益，用作收益计算参考点
            // 从后往前删除，防止出错
            for (int i = profits.size() - 1; i >= chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue() + 1; i--) profits.remove(i);
        }
    }

    /**
     * 轮换成本
     *
     * @param costs          成本列表
     * @param curSettleCost  当前结算周期的业务【质押/委托】成本
     * @param curSettleEpoch 当前结算周期轮数
     * @param chainConfig    链配置
     */
    public static void rotateCost(List<PeriodValueElement> costs, BigDecimal curSettleCost, BigInteger curSettleEpoch, BlockChainConfig chainConfig) {
        // 添加下一周期的质押成本
        if (curSettleEpoch.longValue() == 0) curSettleCost = BigDecimal.ZERO; // 如果当前结算周期是0则成本为0
        PeriodValueElement pve = new PeriodValueElement();
        pve.setPeriod(curSettleEpoch.longValue());
        pve.setValue(curSettleCost);
        costs.add(pve);
        // 保留指定数量最新的记录
        if (costs.size() > chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue() + 1) {
            // 按结算周期由大到小排序
            costs.sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
            // 删除多余的元素
            for (int i = costs.size() - 1; i >= chainConfig.getMaxSettlePeriodCount4AnnualizedRateStat().longValue() + 1; i--) costs.remove(i);
        }
    }

    /**
     * 计算年化率
     *
     * @param profits     利润列表
     * @param costs       成本列表
     * @param chainConfig 链配置
     * @return
     */
    public static BigDecimal calculateAnnualizedRate(List<PeriodValueElement> profits, List<PeriodValueElement> costs, BlockChainConfig chainConfig) {
        // 利润累计按周期从大到小排列
        profits.sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
        // 成本按周期从大到小排列
        costs.sort((c1, c2) -> Integer.compare(0, c1.getPeriod().compareTo(c2.getPeriod())));
        // 最旧利润累计
        PeriodValueElement first = profits.get(profits.size() - 1);
        // 利润=最大收益累计-最小的收益累计
        PeriodValueElement max = first;
        for (PeriodValueElement p : profits) {
            if (p.getValue().compareTo(max.getValue()) > 0) max = p;
        }
        BigDecimal profitSum = max.getValue().subtract(first.getValue()).abs();

        // 最新利润累计
        PeriodValueElement last = profits.get(0);
        BigDecimal costSum = BigDecimal.ZERO;
        for (PeriodValueElement cost : costs) {
            // 跳过大于利润所在最大结算周期的成本周期
            if (cost.getPeriod() > last.getPeriod()) continue;
            costSum = costSum.add(cost.getValue());
        }

        if (costSum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // 年化率=(利润/成本)x每个增发周期的结算周期数x100
        BigDecimal rate = profitSum.divide(costSum, 16, RoundingMode.FLOOR) // 除总成本
                                   .multiply(new BigDecimal(chainConfig.getSettlePeriodCountPerIssue())) // 乘每个增发周期的结算周期数
                                   .multiply(BigDecimal.valueOf(100));
        return rate.setScale(2, RoundingMode.FLOOR);
    }

}
