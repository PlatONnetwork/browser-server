package com.platon.browser.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.platon.browser.dao.entity.BlockNode;

/**
 * @program: BlockNodeBusinessMapper.java
 * @description:
 * @author: Rongjin Zhang
 * @create: 2020/9/22
 */
public interface CustomBlockNodeMapper {
    int batchInsert(@Param("list") List<BlockNode> list);

    int selectMaxNum();

    List<BlockNode> selectNodeByDis(@Param("startNum") Integer startNum, @Param("endNum") Integer endNum);
}