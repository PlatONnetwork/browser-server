package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.node.AppNodeVoteSummaryDto;
import com.platon.browser.dto.app.node.AppUserNodeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomNodeRankingMapper {
    int insertOrUpdate(@Param("list") List<NodeRanking> list);
    List<AppNodeDto> selectByChainIdAndIsValidOrderByRanking(
            @Param("chainId") String chainId,
            @Param("isValid") int isValid
    );

    AppNodeDetailDto detailByChainIdAndNodeId(
            @Param("chainId")String chainId,
            @Param("nodeId") String nodeId
    );
    long getValidVoteCountByNodeIds(
            @Param("chainId")String chainId,
            @Param("nodeIds") List<String> nodeIds
    );
    List<AppUserNodeDto> getNodeListByNodeIds(
            @Param("chainId")String chainId,
            @Param("nodeIds") List<String> nodeIds
    );

    NodeRanking selectByBlockNumber(
            @Param("chainId")String chainId,
            @Param("blockNumber") long blockNumber
    );
}
