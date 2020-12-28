package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.StakingHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface CustomStakingHistoryMapper {

	int batchInsertOrUpdateSelective ( @Param("list") Set <StakingHistory> list, @Param("selective") StakingHistory.Column... selective );



}
