package com.platon.browser.now.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
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
	private NodeMapper nodeMapper;
	
	@Override
	public String getNodeName(String nodeId) {
		NodeExample nodeExample = new NodeExample();
		NodeExample.Criteria criteria = nodeExample.createCriteria();
		criteria.andNodeIdEqualTo(nodeId);
		List<Node> nodes = nodeMapper.selectByExample(nodeExample);
		if(nodes.size() > 0) {
			return nodes.get(0).getNodeName();
		}
		return "";
	}

}
