package com.platon.browser.dao.mapper;

public interface CustomNodeMapper {
	/**
	 * 根据nodeId查询nodeName
	 * @method findNameById
	 * @param nodeId
	 * @return
	 */
    String findNameById(String nodeId);
}