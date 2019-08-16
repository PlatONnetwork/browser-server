package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.Proposal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomNodeOptMapper {

    List<NodeOpt> selectByNodeId(@Param("nodeId")String nodeId);
    List<NodeOpt> selectByNodeIdList(@Param("nodeIds")List<String> nodeIds);
    int batchInsertOrUpdateSelective(@Param("list") Set<NodeOpt> list, @Param("selective") NodeOpt.Column... selective);

}
