package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NodeRanking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomNodeRankingMapper {
    int insertOrUpdate(@Param("list") List<NodeRanking> list);

}