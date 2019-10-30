package com.platon.browser.old.engine.cache;

import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.old.engine.stage.BlockChainStage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/17 20:31
 * @Description:
 */
//@Component
public class CacheHolder {
    // 全量数据(质押相关)，需要根据业务变化，保持与数据库一致
    @Autowired
    private NodeCache nodeCache;
    // 节点名称缓存 <节点ID-节点名称>
    private Map<String,String> nodeNameMap = new HashMap<>();
    // 业务数据暂存容器
    private BlockChainStage stageData = new BlockChainStage();
    // 全量数据(提案相关)，需要根据业务变化，保持与数据库一致
    private ProposalCache proposalCache = new ProposalCache();
    // 全量统计数据
    private CustomNetworkStat networkStatCache = new CustomNetworkStat();
    // 全量数据，需要根据业务变化，保持与数据库一致
    private AddressCache addressCache = new AddressCache();

    public Map<String, String> getNodeNameMap() {
        return nodeNameMap;
    }

    public BlockChainStage getStageData() {
        return stageData;
    }

    public NodeCache getNodeCache() {
        return nodeCache;
    }

    public ProposalCache getProposalCache() {
        return proposalCache;
    }

    public CustomNetworkStat getNetworkStatCache() {
        return networkStatCache;
    }

    public AddressCache getAddressCache() {
        return addressCache;
    }
}
