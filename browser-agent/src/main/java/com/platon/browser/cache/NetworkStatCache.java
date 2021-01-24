package com.platon.browser.cache;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platon.browser.bean.ConfigChange;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;

import lombok.Data;

/**
 * 网络统计缓存
 */
@Component
@Data
public class NetworkStatCache {
    private NetworkStat networkStat = new NetworkStat();

    @Autowired
    private TpsCalcCache tpsCalcCache;

    /**
     * 基于区块维度更新网络统计信息
     * 
     * @param block
     * @param proposalQty
     */
    public void updateByBlock(Block block, int proposalQty) {
        this.tpsCalcCache.update(block);
        int tps = this.tpsCalcCache.getTps();
        int maxTps = this.tpsCalcCache.getMaxTps();
        this.networkStat.setTxQty(block.getTransactions().size() + this.networkStat.getTxQty());
        this.networkStat.setErc20TxQty(block.getErc20TxQty() + this.networkStat.getErc20TxQty());
        this.networkStat.setErc721TxQty(block.getErc721TxQty() + this.networkStat.getErc721TxQty());
        this.networkStat.setProposalQty(proposalQty + this.networkStat.getProposalQty());
        this.networkStat.setCurTps(tps);
        this.networkStat.setCurBlockHash(block.getHash());
        if (maxTps > this.networkStat.getMaxTps()) {
            this.networkStat.setMaxTps(maxTps);
        }
    }

    /**
     * 获得操作日志需要
     * 
     * @return
     */
    public long getAndIncrementNodeOptSeq() {
        long seq = this.networkStat.getNodeOptSeq() == null ? 1 : this.networkStat.getNodeOptSeq() + 1;
        this.networkStat.setNodeOptSeq(seq);
        return seq;
    }

    /**
     * 基于任务更新网络统计信息
     * 
     * @param issueValue
     * @param turnValue
     * @param totalValue
     * @param stakingValue
     * @param addressQty
     * @param doingProposalQty
     */
    public void updateByTask(BigDecimal issueValue, BigDecimal turnValue, BigDecimal availableStaking,
        BigDecimal totalValue, BigDecimal stakingValue, int addressQty, int doingProposalQty,
        BigDecimal stakingReward) {
        this.networkStat.setIssueValue(issueValue);
        this.networkStat.setTurnValue(turnValue);
        this.networkStat.setAvailableStaking(availableStaking);
        this.networkStat.setStakingDelegationValue(totalValue);
        this.networkStat.setStakingValue(stakingValue);
        this.networkStat.setAddressQty(addressQty);
        this.networkStat.setDoingProposalQty(doingProposalQty);
        this.networkStat.setStakingReward(stakingReward);
    }

    /**
     * 基于增发或结算周期变更更新网络统计信息
     */
    public void updateByEpochChange(ConfigChange configChange) {
        if (configChange.getBlockReward() != null)
            this.networkStat.setBlockReward(configChange.getBlockReward());
        if (configChange.getYearStartNum() != null)
            this.networkStat.setAddIssueBegin(configChange.getYearStartNum().longValue());
        if (configChange.getYearEndNum() != null)
            this.networkStat.setAddIssueEnd(configChange.getYearEndNum().longValue());
        if (configChange.getSettleStakeReward() != null)
            this.networkStat.setSettleStakingReward(configChange.getSettleStakeReward());
        if (configChange.getStakeReward() != null)
            this.networkStat.setStakingReward(configChange.getStakeReward());
        if (configChange.getAvgPackTime() != null)
            this.networkStat.setAvgPackTime(configChange.getAvgPackTime().longValue());
        if (StringUtils.isNotBlank(configChange.getIssueRates()))
            this.networkStat.setIssueRates(configChange.getIssueRates());
    }

    public void init(NetworkStat networkStat) {
        this.networkStat = networkStat;
    }
}
