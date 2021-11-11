package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomRpPlan;
import com.platon.browser.dao.entity.RpPlan;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CustomRpPlanMapper {

    List<CustomRpPlan> selectAll();

    int batchInsertOrUpdateSelective(@Param("list") Set<RpPlan> list, @Param("selective") RpPlan.Column... selective);

    /**
     * 查询金额总数
     */
    BigDecimal selectSumByAddress(String address);

    BigDecimal sumAmountByAddressAndBlockNumber(@Param("address") String address, @Param("blockNumber") Long blockNumber);

    /**
     * 锁仓未到期的金额
     *
     * @param settlePeriodBlockCount: 每个结算周期区块总数
     * @param curBlockNumber:         当前块高
     * @return: java.math.BigDecimal
     * @date: 2021/11/11
     */
    BigDecimal getRPNotExpiredValue(@Param("settlePeriodBlockCount") Long settlePeriodBlockCount, @Param("curBlockNumber") Long curBlockNumber);

}
