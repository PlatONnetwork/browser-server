package com.platon.browser.service;

import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.node.NodeItem;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;

import java.util.List;

public interface NodeService {

    List<NodeInfo> getNodeInfoList();

    List<NodeItem> getNodeItemList(NodeListReq req);

    NodeDetail getNodeDetail(NodeDetailReq req);

    List<BlockItem> getBlockList(BlockListReq req);
}
