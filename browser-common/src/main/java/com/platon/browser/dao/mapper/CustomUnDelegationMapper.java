package com.platon.browser.dao.mapper;

import com.platon.browser.dto.UnDelegationBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomUnDelegationMapper {

    List<UnDelegationBean> selectByNodeId(@Param("nodeId") String nodeId);
    List<UnDelegationBean> selectByNodeIdList(@Param("nodeIds") List<String> nodeIds);

}
