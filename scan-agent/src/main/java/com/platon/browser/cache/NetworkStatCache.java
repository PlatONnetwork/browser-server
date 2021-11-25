package com.platon.browser.cache;

import cn.hutool.core.util.ObjectUtil;
import com.platon.browser.bean.ConfigChange;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 网络统计缓存
 */
@Slf4j
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
        Integer erc20TxQty = 0;
        if (this.networkStat.getErc20TxQty() != null) {
            erc20TxQty = this.networkStat.getErc20TxQty();
        }
        erc20TxQty = block.getErc20TxQty() + erc20TxQty;
        Integer erc721TxQty = 0;
        if (this.networkStat.getErc721TxQty() != null) {
            erc721TxQty = this.networkStat.getErc721TxQty();
        }
        erc721TxQty = block.getErc721TxQty() + erc721TxQty;
        this.networkStat.setErc20TxQty(erc20TxQty);
        this.networkStat.setErc721TxQty(erc721TxQty);
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
     * @param turnValue:
     * @param availableStaking:
     * @param totalValue:
     * @param stakingValue:
     * @param addressQty:
     * @param doingProposalQty:
     * @return: void
     * @date: 2021/11/24
     */
    public void updateByTask(BigDecimal turnValue, BigDecimal availableStaking, BigDecimal totalValue, BigDecimal stakingValue, int addressQty, int doingProposalQty) {
        if (ObjectUtil.isNotNull(turnValue) && turnValue.compareTo(BigDecimal.ZERO) > 0) {
            this.networkStat.setTurnValue(turnValue);
        }
        this.networkStat.setAvailableStaking(availableStaking);
        this.networkStat.setStakingDelegationValue(totalValue);
        this.networkStat.setStakingValue(stakingValue);
        this.networkStat.setAddressQty(addressQty);
        this.networkStat.setDoingProposalQty(doingProposalQty);
    }

    /**
     * 基于增发或结算周期变更更新网络统计信息
     */
    public void updateByEpochChange(ConfigChange configChange) {
        if (configChange.getBlockReward() != null) this.networkStat.setBlockReward(configChange.getBlockReward());
        if (configChange.getYearStartNum() != null) this.networkStat.setAddIssueBegin(configChange.getYearStartNum().longValue());
        if (configChange.getYearEndNum() != null) this.networkStat.setAddIssueEnd(configChange.getYearEndNum().longValue());
        if (configChange.getSettleStakeReward() != null) this.networkStat.setSettleStakingReward(configChange.getSettleStakeReward());
        if (configChange.getStakeReward() != null) this.networkStat.setStakingReward(configChange.getStakeReward());
        if (configChange.getAvgPackTime() != null) this.networkStat.setAvgPackTime(configChange.getAvgPackTime().longValue());
        if (StringUtils.isNotBlank(configChange.getIssueRates())) this.networkStat.setIssueRates(configChange.getIssueRates());
    }

    /**
     * 初始化网络缓存
     *
     * @param networkStat
     * @return void
     * @date 2021/4/19
     */
    public void init(NetworkStat networkStat) {
        log.info("初始化网络缓存");
        this.networkStat = networkStat;
    }

}
