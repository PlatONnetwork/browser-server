package com.platon.browser.common.complement.cache;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platon.browser.dao.entity.NetworkStat;

import lombok.Data;

@Component
@Data
public class NetworkStatCache {
    private NetworkStat networkStat;
    
    @Autowired
    private TpsCalcCache tpsCalcCache;
    
    
    /**
     * 基于区块维度更新网络统计信息
     * @param txQty
     * @param proposalQty
     * @param time
     */
    public void updateByBlock(int txQty, int proposalQty, Date time) {
    	tpsCalcCache.update(txQty, time.getTime());
    	int tps = tpsCalcCache.getTps();
    	networkStat.setTxQty(txQty);
    	networkStat.setProposalQty(proposalQty);
    	networkStat.setCurTps(tps);
    	if(tps > networkStat.getMaxTps()) {
    		networkStat.setMaxTps(tps);
    	}
    }
    
    /**
     * 获得操作日志需要
     * @return
     */
    public long getAndIncrementNodeOptSeq() {
    	long seq = networkStat.getNodeOptSeq() == null? 1: networkStat.getNodeOptSeq() + 1;
    	networkStat.setNodeOptSeq(seq);
    	return seq;
    }    
}
