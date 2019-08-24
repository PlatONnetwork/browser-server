package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.NetworkStat;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/21
 * Time: 17:39
 */
@Data
public class NetworkStatStage {
    private Set <NetworkStat> networkStatInsertStage = new HashSet <>();
    private Set <NetworkStat> networkStatUpdateStage = new HashSet <>();
    public void insertNetworkStat( NetworkStat networkStat){
        networkStatInsertStage.add(networkStat);
    }
    public void updateNetworkStat( NetworkStat networkStat){
        networkStatUpdateStage.add(networkStat);
    }
    public void clear() {
        networkStatInsertStage.clear();
        networkStatUpdateStage.clear();
    }

    public Set<NetworkStat> getNetworkStatInsertStage() {
        return networkStatInsertStage;
    }

    public Set<NetworkStat> getNetworkStatUpdateStage() {
        return networkStatUpdateStage;
    }
}
