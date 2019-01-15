package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CutsomNodeRankingMapper {
    int insertOrUpdate(@Param("list") List<NodeRanking> list);

}