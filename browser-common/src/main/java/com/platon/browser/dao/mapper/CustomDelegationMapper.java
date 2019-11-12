package com.platon.browser.dao.mapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.github.pagehelper.Page;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.DelegationAddress;
import com.platon.browser.dto.DelegationStaking;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomDelegationMapper {

    List<CustomDelegation> selectByNodeId ( @Param("nodeId") String nodeId );
    List<CustomDelegation> selectByNodeIdList ( @Param("nodeIds") List <String> nodeIds );
    Page<DelegationStaking> selectStakingByNodeId ( @Param("nodeId") String nodeId);
    Page<DelegationAddress> selectAddressByAddr ( @Param("delegateAddr") String delegateAddr );

}
