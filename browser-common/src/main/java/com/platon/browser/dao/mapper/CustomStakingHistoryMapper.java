package com.platon.browser.dao.mapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingHistory;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.StakingNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomStakingHistoryMapper {

	int batchInsertOrUpdateSelective ( @Param("list") Set <StakingHistory> list, @Param("selective") StakingHistory.Column... selective );



}
