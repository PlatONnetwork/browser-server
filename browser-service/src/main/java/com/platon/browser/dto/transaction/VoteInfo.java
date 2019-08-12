package com.platon.browser.dto.transaction;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.ticket.TxInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: dongqile
 * Date: 2019/3/18
 * Time: 10:20
 */
@Data
public class VoteInfo {
    private String hash;
    private String nodeId;
    private String nodeName;
    private Integer vailVoteCount;
    private Integer voteSum;
    private Date deadLine;
    private String walletAddress;
    private String price;
    private BigDecimal income;
    private String blockHash;
    public  void init( TransactionWithBLOBs transaction){
        TxInfo ticketTxInfo = JSON.parseObject(transaction.getTxInfo(), TxInfo.class);
        TxInfo.Parameter ticketParameter = ticketTxInfo.getParameters();
        this.nodeId = ticketParameter.getNodeId();
        this.voteSum = ticketParameter.getCount();
        this.walletAddress = transaction.getFrom();
        this.hash = transaction.getHash();
        this.blockHash = transaction.getBlockHash();
    }
}
