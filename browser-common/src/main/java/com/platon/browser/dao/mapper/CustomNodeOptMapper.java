package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dto.CustomNodeOpt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomNodeOptMapper {

    List<CustomNodeOpt> selectByNodeId(@Param("nodeId")String nodeId);
    List<CustomNodeOpt> selectByNodeIdList(@Param("nodeIdList")List<String> nodeIdList);
    List<CustomNodeOpt> selectByTxHash(@Param("txHash")String txHash);
    List<CustomNodeOpt> selectByTxHashList(@Param("txHashList")List<String> txHashList);
    int batchInsertOrUpdateSelective(@Param("list") Set<NodeOpt> list, @Param("selective") NodeOpt.Column... selective);

}
