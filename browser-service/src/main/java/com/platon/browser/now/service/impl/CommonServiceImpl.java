package com.platon.browser.now.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.dao.mapper.CustomNodeMapper;
import com.platon.browser.now.service.CommonService;

/**
 * 实现
 *  @file CommonServiceImpl.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月11日
 */
@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private CustomNodeMapper customNodeMapper;
	
	@Override
	public String getNodeName(String nodeId,String nodeName) {
		/**
    	 * 当nodeId为空或者nodeName不为空则直接返回name
    	 */
    	if(StringUtils.isNotBlank(nodeName) || StringUtils.isBlank(nodeId)) {
    		return nodeName;
    	}
		return customNodeMapper.findNameById(nodeId);
	}

}
