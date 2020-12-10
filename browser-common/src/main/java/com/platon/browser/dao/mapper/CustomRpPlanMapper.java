package com.platon.browser.dao.mapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.platon.browser.dao.entity.RpPlan;
import com.platon.browser.dto.CustomRpPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper
public interface CustomRpPlanMapper {
    List<CustomRpPlan> selectAll ();
    int batchInsertOrUpdateSelective (@Param("list") Set <RpPlan> list, @Param("selective") RpPlan.Column... selective );
    /**
     * 查询金额总数
     */
    BigDecimal selectSumByAddress ( String address );
}
