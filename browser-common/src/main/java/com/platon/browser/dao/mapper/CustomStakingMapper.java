package com.platon.browser.dao.mapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.CustomStaking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomStakingMapper {
	List<CustomStaking> selectByNodeId ( @Param("nodeId") String nodeId );

	List<CustomStaking> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );

	int batchInsertOrUpdateSelective (@Param("list") Set <Staking> list, @Param("selective") Staking.Column... selective );

	String selectSumExitDelegate ();
	
	Integer selectCountByActive ();

}
