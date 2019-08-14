package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Slash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomSlashMapper {

    List<Slash> selectByNodeId(@Param("nodeId") String nodeId);
    List<Slash> selectByNodeIdList(@Param("nodeIds") List<String> nodeIds);

}
