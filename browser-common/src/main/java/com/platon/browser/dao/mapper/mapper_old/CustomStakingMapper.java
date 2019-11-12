package com.platon.browser.dao.mapper.mapper_old;

import com.platon.browser.dto.CustomStaking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomStakingMapper {
	List<CustomStaking> selectByNodeId ( @Param("nodeId") String nodeId );

	List<CustomStaking> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );

}
