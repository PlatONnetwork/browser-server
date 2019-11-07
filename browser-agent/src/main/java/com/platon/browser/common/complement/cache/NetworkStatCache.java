package com.platon.browser.common.complement.cache;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.platon.browser.dao.entity.NetworkStat;

import lombok.Data;

@Component
@Data
public class NetworkStatCache {
    private NetworkStat networkStat;
    
    /**
     * 基于区块维度更新网络统计信息
     * @param txQty
     * @param proposalQty
     * @param time
     */
    public void updateByBlock(int txQty, int proposalQty, Date time) {
    	//TODO
    }
    
    
}
