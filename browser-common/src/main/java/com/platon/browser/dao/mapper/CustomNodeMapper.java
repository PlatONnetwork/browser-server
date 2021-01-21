package com.platon.browser.dao.mapper;

import com.github.pagehelper.Page;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import org.apache.ibatis.annotations.Mapper;

public interface CustomNodeMapper {
	/**
	 * 根据nodeId查询nodeName
	 * @method findNameById
	 * @param nodeId
	 * @return
	 */
    String findNameById(String nodeId);
    
    /**
     * 根据条件查询列表
     * @method selectByExample
     * @param example
     * @return
     */
    Page<Node> selectListByExample(NodeExample example);
    
    
    int selectCountByActive();
}