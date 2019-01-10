package com.platon.browser.service;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.req.node.NodePageReq;

import java.util.List;
import java.util.Set;

public interface NodeService {
    RespPage<NodeListItem> getPage(NodePageReq req);
    NodeDetail getDetail(NodeDetailReq req);
    void updatePushData(String chainId, Set<NodeRanking> data);
    List<NodePushItem> getPushData(String chainId);

    List<BlockListItem> getBlockList(BlockListReq req);
}
