package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Slash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomSlashMapper {
    List<Slash> selectByNodeId(@Param("nodeId") String nodeId);
    List<Slash> selectByNodeIdList(@Param("nodeIds") List<String> nodeIds);
    int batchInsertOrUpdateSelective(@Param("list") Set<Slash> list, @Param("selective") Slash.Column... selective);
}
