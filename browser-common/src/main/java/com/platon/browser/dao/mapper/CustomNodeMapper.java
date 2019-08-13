package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.entity.Staking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomNodeMapper {

    List<Node> selectVerifiers();
    List<Node> selectValidators();

}
