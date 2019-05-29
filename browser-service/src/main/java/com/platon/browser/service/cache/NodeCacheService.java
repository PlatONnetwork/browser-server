package com.platon.browser.service.cache;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dto.node.NodePushItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NodeCacheService {
    void clearNodePushCache(String chainId);
    void updateNodePushCache(String chainId, Set<NodeRanking> items);
    void resetNodePushCache(String chainId, boolean clearOld);
    List<NodePushItem> getNodePushCache(String chainId);
    void updateNodeIdMaxBlockNum(String chainId, Map<String,Long> nodeIdMaxBlockNumMap);
    String getNodeMaxBlockNum(String chainId,String nodeId);
}
