package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dto.CustomDelegation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomDelegationMapper {

    List<CustomDelegation> selectByNodeId(@Param("nodeId")String nodeId);
    List<CustomDelegation> selectByNodeIdList(@Param("nodeIds")List<String> nodeIds);
    int batchInsertOrUpdateSelective(@Param("list") Set<Delegation> list, @Param("selective") Delegation.Column ... selective);
}
