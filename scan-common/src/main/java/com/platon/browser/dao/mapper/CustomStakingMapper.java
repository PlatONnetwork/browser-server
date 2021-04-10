package com.platon.browser.dao.mapper;

import com.platon.browser.bean.CustomStaking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomStakingMapper {
	List<CustomStaking> selectByNodeId ( @Param("nodeId") String nodeId );

	List<CustomStaking> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );

	String selectSumExitDelegate ();
	
	Integer selectCountByActive ();

}
