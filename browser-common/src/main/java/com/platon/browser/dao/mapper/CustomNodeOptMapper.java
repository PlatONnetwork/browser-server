package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dto.NodeBean;
import com.platon.browser.dto.StakingBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomNodeOptMapper {

    List<NodeOpt> selectByNodeId(@Param("nodeId")String nodeId);
    List<NodeOpt> selectByNodeIdList(@Param("nodeIds")List<String> nodeIds);

}
