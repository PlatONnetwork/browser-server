package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.bean.CustomRpPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CustomRpPlanMapper {
    List<CustomRpPlan> selectAll ();
    int batchInsertOrUpdateSelective (@Param("list") Set <RpPlan> list, @Param("selective") RpPlan.Column... selective );
    /**
     * 查询金额总数
     */
    BigDecimal selectSumByAddress ( String address );
    BigDecimal sumAmountByAddressAndBlockNumber ( @Param("address")String address,@Param("blockNumber") Long blockNumber);
}
