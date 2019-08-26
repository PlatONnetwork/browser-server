package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.NetworkStat;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 统计信息新增或修改暂存类，入库后各容器需要清空
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


    public Set<NetworkStat> exportNetworkStat(){
        Set<NetworkStat> returnData = new HashSet<>(networkStatInsertStage);
        returnData.addAll(networkStatUpdateStage);
        return returnData;
    }
    public Set<NetworkStat> getNetworkStatInsertStage() {
        return networkStatInsertStage;
    }

    public Set<NetworkStat> getNetworkStatUpdateStage() {
        return networkStatUpdateStage;
    }


}
