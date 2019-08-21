package com.platon.browser.engine.result;

import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dto.CustomNetworkStat;
import com.platon.browser.dto.CustomNode;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 17:39
 */
@Data
public class NetworkStatResult {
    private Set <NetworkStat> updateNetworkStats = new HashSet <>();
    public void stageUpdateNetworkStat( NetworkStat networkStat){
        updateNetworkStats.add(networkStat);
    }
    public void clear() {
        updateNetworkStats.clear();
    }
}