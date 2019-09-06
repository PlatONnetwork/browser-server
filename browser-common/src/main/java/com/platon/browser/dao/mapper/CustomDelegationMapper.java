package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.DelegationStaking;
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
    Page<DelegationStaking> selectDelegationAndStakingByExample(@Param("nodeId") String nodeId,@Param("stakingBlockNum") Long stakingBlockNum,@Param("delegateAddr") String delegateAddr);
	List<DelegationStaking> selectSumDelegateByExample(@Param("nodeId") String nodeId,@Param("stakingBlockNum") Long stakingBlockNum);
	List<DelegationStaking> selectSumDelegateByAddress(@Param("delegateAddr") String delegateAddr);

}
