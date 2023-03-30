package com.platon.browser.bean;

import com.platon.browser.dao.entity.NetworkStat;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @description: 网络统计类
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-06 10:33:32
 **/
@Slf4j
public class CollectionNetworkStat extends NetworkStat {
    private CollectionNetworkStat() {}

    public static CollectionNetworkStat newInstance() {
        CollectionNetworkStat stat = new CollectionNetworkStat();
        //Date date = new Date();
        //stat.setCreateTime(date);
        //stat.setUpdateTime(date);
        stat.setAddIssueBegin(0L);
        stat.setAddIssueEnd(0L);
        stat.setAddressQty(0);
        stat.setBlockReward(BigDecimal.ZERO);
        stat.setCurNumber(0L);
        stat.setCurBlockHash("");
        stat.setCurTps(0);
        stat.setDoingProposalQty(0);
        stat.setIssueValue(BigDecimal.ZERO);
        stat.setNextSettle(0L);
        stat.setNodeId("");
        stat.setNodeName("");
        stat.setMaxTps(0);
        stat.setProposalQty(0);
        stat.setStakingDelegationValue(BigDecimal.ZERO);
        stat.setTxQty(0);
        stat.setErc20TxQty(0);
        stat.setErc721TxQty(0);
        stat.setTurnValue(BigDecimal.ZERO);
        stat.setAvailableStaking(BigDecimal.ZERO);
        stat.setStakingValue(BigDecimal.ZERO);
        stat.setSettleStakingReward(BigDecimal.ZERO);
        stat.setStakingReward(BigDecimal.ZERO);
        stat.setStakingDelegationValue(BigDecimal.ZERO);
        stat.setNodeOptSeq(0L);
        return stat;
    }
}
