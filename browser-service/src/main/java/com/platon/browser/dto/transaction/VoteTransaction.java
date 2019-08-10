package com.platon.browser.dto.transaction;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.ticket.TxInfo;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * User: dongqile
 * Date: 2019/3/15
 * Time: 16:00
 */
@Data
public class VoteTransaction {
    private String transactionHash;
    private String candidateId;
    private String owner;
    private String earnings;
    private String transactiontime;
    private String deposit;
    private String totalTicketNum;
    private String validNum;
    public void init( TransactionWithBLOBs transaction ){
        TxInfo ticketTxInfo = JSON.parseObject(transaction.getTxInfo(), TxInfo.class);
        TxInfo.Parameter ticketParameter = ticketTxInfo.getParameters();
        if(ticketParameter!=null){
            this.candidateId = ticketParameter.getNodeId();
            this.deposit = String.valueOf(ticketParameter.getPrice());
            this.totalTicketNum = String.valueOf(ticketParameter.getCount());
        }
        this.owner = transaction.getFrom();
        this.transactionHash = transaction.getHash();
        //todo:0.7版本修改成时间戳
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transaction.getTimestamp());
        this.transactiontime = dateStr;
    }

}
