package com.platon.browser.common.complement.cache;

import com.platon.browser.common.service.epoch.ConfigChange;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 网络统计缓存
 */
@Component
@Data
public class NetworkStatCache {
    private NetworkStat networkStat=new NetworkStat();
    
    @Autowired
    private TpsCalcCache tpsCalcCache;

    /**
     * 基于区块维度更新网络统计信息
     * @param block
     * @param proposalQty
     */
    public void updateByBlock(Block block,int proposalQty) {
    	tpsCalcCache.update(block);
    	int tps = tpsCalcCache.getTps();
    	networkStat.setTxQty(block.getTransactions().size()+networkStat.getTxQty());
    	networkStat.setProposalQty(proposalQty+networkStat.getProposalQty());
    	networkStat.setCurTps(tps);
    	networkStat.setCurBlockHash(block.getHash());
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

    /**
     * 基于任务更新网络统计信息
     * @param issueValue
     * @param turnValue
     * @param totalValue
     * @param stakingValue
     * @param addressQty
     * @param doingProposalQty
     */
	public void updateByTask(BigDecimal issueValue, BigDecimal turnValue, BigDecimal totalValue, BigDecimal stakingValue, int addressQty, int doingProposalQty,BigDecimal stakingReward) {
		networkStat.setIssueValue(issueValue);
		networkStat.setTurnValue(turnValue);
		networkStat.setStakingDelegationValue(totalValue);
		networkStat.setStakingValue(stakingValue);
		networkStat.setAddressQty(addressQty);
		networkStat.setDoingProposalQty(doingProposalQty);
		networkStat.setStakingReward(stakingReward);
	}

	/**
	 * 基于增发或结算周期变更更新网络统计信息
	 */
	public void updateByEpochChange(ConfigChange configChange) {
		if(configChange.getBlockReward()!=null) networkStat.setBlockReward(configChange.getBlockReward());
		if(configChange.getYearStartNum()!=null) networkStat.setAddIssueBegin(configChange.getYearStartNum().longValue());
		if(configChange.getYearEndNum()!=null) networkStat.setAddIssueEnd(configChange.getYearEndNum().longValue());
		if(configChange.getSettleStakeReward()!=null) networkStat.setSettleStakingReward(configChange.getSettleStakeReward());
		if(configChange.getBlockReward()!=null) networkStat.setBlockReward(configChange.getBlockReward());
		if(configChange.getStakeReward()!=null) networkStat.setStakingReward(configChange.getStakeReward());
	}

    public void init(NetworkStat networkStat) {
	    this.networkStat=networkStat;
    }
}
