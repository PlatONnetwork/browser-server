package com.platon.browser.service;

import com.platon.browser.dto.NodeRespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;

import java.util.List;
import java.util.Map;

public interface NodeService {
    void clearPushCache(String chainId);
    List<BlockListItem> getBlockList(BlockListReq req);
    Map<String,String> getNodeNameMap(String chainId, List<String> nodeIds);

    void updateLocalNodeCache(String chainId);
}
