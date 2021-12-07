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
     */
    public void updateByBlock(Block block) {
        this.tpsCalcCache.updateIfNotHandle(block);
        int tps = this.tpsCalcCache.getTps();
        int maxTps = this.tpsCalcCache.getMaxTps();
        this.networkStat.setCurTps(tps);
        if (maxTps > this.networkStat.getMaxTps()) {
            this.networkStat.setMaxTps(maxTps);
        }
    }

    /**
     * 基于任务更新网络统计信息
     *
     * @param turnValue:
     * @param availableStaking:
     * @param totalValue: 实时质押委托总数
     * @param stakingValue: 实时质押总数
     * @return: void
     * @date: 2021/11/24
     */
    public void updateByTask(BigDecimal turnValue, BigDecimal availableStaking, BigDecimal totalValue, BigDecimal stakingValue) {
        if (ObjectUtil.isNotNull(turnValue) && turnValue.compareTo(BigDecimal.ZERO) > 0) {
            this.networkStat.setTurnValue(turnValue);
        }
        this.networkStat.setAvailableStaking(availableStaking);
        this.networkStat.setStakingDelegationValue(totalValue);
        this.networkStat.setStakingValue(stakingValue);
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
        this.networkStat = networkStat;
    }

}
