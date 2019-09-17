package com.platon.browser.engine.util;

import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.engine.cache.AddressCache;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.stage.BlockChainStage;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/17 20:31
 * @Description:
 */
public class CacheTool {
    // 节点名称缓存 <节点ID-节点名称>
    public static final Map<String,String> NODE_NAME_MAP = new HashMap<>();
    // 业务数据暂存容器
    public static final BlockChainStage STAGE_DATA = new BlockChainStage();
    // 全量数据(质押相关)，需要根据业务变化，保持与数据库一致
    public static final NodeCache NODE_CACHE = new NodeCache();
    // 全量数据(提案相关)，需要根据业务变化，保持与数据库一致
    public static final ProposalCache PROPOSALS_CACHE = new ProposalCache();
    // 全量统计数据
    public static final CustomNetworkStat NETWORK_STAT_CACHE = new CustomNetworkStat();
    // 全量数据，需要根据业务变化，保持与数据库一致
    public static final AddressCache ADDRESS_CACHE = new AddressCache();
}
