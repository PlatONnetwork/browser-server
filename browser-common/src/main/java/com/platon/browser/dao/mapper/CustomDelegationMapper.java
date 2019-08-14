package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dto.DelegationBean;
import com.platon.browser.dto.NodeBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomDelegationMapper {

    List<DelegationBean> selectByNodeId(@Param("nodeId")String nodeId);
    List<DelegationBean> selectByNodeIdList(@Param("nodeIds")List<String> nodeIds);

}
