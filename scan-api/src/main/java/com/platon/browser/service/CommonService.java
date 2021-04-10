package com.platon.browser.service;

import com.platon.browser.dao.mapper.CustomNodeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 实现
 *  @file CommonServiceImpl.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年11月11日
 */
@Service
public class CommonService {

	@Resource
	private CustomNodeMapper customNodeMapper;
	
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
