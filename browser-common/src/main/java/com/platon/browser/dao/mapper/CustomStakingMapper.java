package com.platon.browser.dao.mapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.StakingNode;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomStakingMapper {
	List<CustomStaking> selectByNodeId ( @Param("nodeId") String nodeId );

	List<CustomStaking> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );

	int batchInsertOrUpdateSelective (@Param("list") Set <Staking> list, @Param("selective") Staking.Column... selective );

	Page<StakingNode> selectStakingAndNodeByExample ( @Param("nodeId") String node_id, @Param("name") String name,
                                                      @Param("status") Integer status, @Param("isConsensus") Integer isConsensus, @Param("isSetting") Integer isSetting );

	List<StakingNode> selectStakingAndNodeByNodeId ( @Param("nodeId") String node_id );

	List<StakingNode> selectStakingAndNodeActive ( @Param("nodeId") String node_id, @Param("status") Integer status );

	Page<StakingNode> selectHistoryNode ( @Param("key") String key, @Param("statusList") List <Integer> statusList );

	String selectSumExitDelegate ();

}
