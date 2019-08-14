package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.StakingBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomStakingMapper {
    List<StakingBean> selectByNodeId(@Param("nodeId")String nodeId);
    List<StakingBean> selectByNodeIdList(@Param("nodeIds")List<String> nodeIds);
}
