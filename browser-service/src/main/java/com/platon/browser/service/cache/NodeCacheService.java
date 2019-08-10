package com.platon.browser.service.cache;

import java.util.List;
import java.util.Map;

public interface NodeCacheService {
    void clearNodePushCache(String chainId);

    void resetNodePushCache(String chainId, boolean clearOld);
    void updateNodeIdMaxBlockNum(String chainId, Map<String,Long> nodeIdMaxBlockNumMap);
    String getNodeMaxBlockNum(String chainId,String nodeId);
}
