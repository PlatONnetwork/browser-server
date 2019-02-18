package com.platon.browser.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NodeRespPage<T> extends RespPage<T> {
    private long voteCount;//投票数
    private BigDecimal proportion;//占比
    private BigDecimal blockReward;//每个区块奖励
    private BigDecimal ticketPrice;//票价
    private long selectedNodeCount; //已选中节点
    private long totalNodeCount=200;//总竞选节点
    private BigDecimal lowestDeposit;//最低质押
    private BigDecimal highestDeposit;//最高质押
}
