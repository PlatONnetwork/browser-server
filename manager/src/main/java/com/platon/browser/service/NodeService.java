package com.platon.browser.service;

import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;

import java.util.List;

public interface NodeService {

    List<NodePushItem> getNodeInfoList();

    List<NodeListItem> getNodeItemList(NodeListReq req);

    NodeDetail getNodeDetail(NodeDetailReq req, boolean byName);

    List<BlockListItem> getBlockList(BlockListReq req);
}
