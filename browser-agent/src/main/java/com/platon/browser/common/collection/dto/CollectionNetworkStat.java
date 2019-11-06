package com.platon.browser.common.collection.dto;

import com.platon.browser.dao.entity.NetworkStat;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 网络统计类
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:33:32
 **/
@Data
@Slf4j
@Accessors(chain = true)
public class CollectionNetworkStat extends NetworkStat {
    private CollectionNetworkStat(){}
    public static CollectionNetworkStat newInstance(){
        CollectionNetworkStat stat = new CollectionNetworkStat();
        Date date = new Date();
        stat.setCreateTime(date);
        stat.setUpdateTime(date);
        stat.setAddIssueBegin(0L);
        stat.setAddIssueEnd(0L);
        stat.setAddressQty(0);
        stat.setBlockReward(BigDecimal.ZERO);
        stat.setCurNumber(0L);
        stat.setCurTps(0);
        stat.setDoingProposalQty(0);
        stat.setIssueValue(BigDecimal.ZERO);
        stat.setNextSettle(0L);
        stat.setNodeId("");
        stat.setNodeName("");
        stat.setMaxTps(0);
        stat.setProposalQty(0);
        stat.setStakingDelegationValue(BigDecimal.ZERO);
        return stat;
    }
}
