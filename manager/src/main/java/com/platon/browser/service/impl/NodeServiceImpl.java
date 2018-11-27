package com.platon.browser.service.impl;

import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.NodeExample;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.node.NodeItem;
import com.platon.browser.req.node.NodeBlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    private final Logger logger = LoggerFactory.getLogger(NodeServiceImpl.class);

    @Autowired
    private NodeMapper nodeMapper;

    @Override
    public List<NodeInfo> getNodeInfoList() {
        List<Node> nodeList = nodeMapper.selectByExample(new NodeExample());
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfoList.add(nodeInfo);
            BeanUtils.copyProperties(node,nodeInfo);
        });
        return nodeInfoList;
    }

    @Override
    public List<NodeItem> getNodeItemList(NodeListReq req) {
        return null;
    }

    @Override
    public NodeDetail getNodeDetail(NodeDetailReq req) {
        return null;
    }

    @Override
    public List<BlockItem> getBlockList(NodeBlockListReq req) {
        return null;
    }


}
