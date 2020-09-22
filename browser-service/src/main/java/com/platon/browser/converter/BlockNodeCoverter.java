package com.platon.browser.converter;

import com.platon.browser.dao.entity.BlockNode;
import com.platon.sdk.contracts.ppos.dto.resp.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: browser-server
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020-09-22 18:34
 */
@Mapper
public interface BlockNodeCoverter {
    BlockNodeCoverter INSTANCE = Mappers.getMapper(BlockNodeCoverter.class);

    @Mappings({
            @Mapping(source = "stakingConsensusEpoch",target = "stakingConsensusEpoch")
    })
    BlockNode domain2dto(Node person, Integer stakingConsensusEpoch);

    List<BlockNode> domain2dto(List<Node> nodesList);
}
