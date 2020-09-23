package com.platon.browser.dao.mapper;

import org.apache.ibatis.annotations.Param;

/**
*@program: BlockNodeBusinessMapper.java
*@description: 
*@author: Rongjin Zhang
*@create: 2020/9/22
*/
public interface CustomBlockNodeMapper {
    int batchInsert(@Param("list") List<BlockNode> list);

    int selectMaxNum();
}