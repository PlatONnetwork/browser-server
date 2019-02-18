package com.platon.browser.dto.transaction;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.util.EnergonUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户详情中的交易列表Bean
 */
@Data
public class AccTransactionItem {
    private String txHash;
    private long blockTime;
    private String from;
    private String to;
    private String value;
    private String actualTxCost;
    private int txReceiptStatus;
    private String txType;
    private String txInfo;
    private long serverTime;
    private String failReason;
    private String receiveType;
    private BigDecimal ticketPrice;
    private BigDecimal income;
    private long voteCount;
    private BigDecimal deposit;
    private String nodeId;
    private String nodeName;

    @JsonIgnore
    private Date timestamp;

    public void init(Object initData){
        BeanUtils.copyProperties(initData,this);
        this.setServerTime(System.currentTimeMillis());

        String txHash = "",value = "0",cost = "0";
        if(initData instanceof TransactionWithBLOBs){
            TransactionWithBLOBs transaction = (TransactionWithBLOBs)initData;
            // 交易生成的时间就是出块时间
            this.setBlockTime(transaction.getTimestamp().getTime());
            txHash = transaction.getHash();
            value = transaction.getValue();
            cost = transaction.getActualTxCost();
        }

        if(initData instanceof PendingTx){
            PendingTx pendingTx = (PendingTx)initData;
            this.setTxReceiptStatus(-1); // 手动设置交易状态为pending
            txHash = pendingTx.getHash();
            value = pendingTx.getValue();
        }

        this.setTxHash(txHash);
        BigDecimal v = Convert.fromWei(value, Convert.Unit.ETHER);
        this.setValue(EnergonUtil.format(v));
        v = Convert.fromWei(cost, Convert.Unit.ETHER);
        this.setActualTxCost(EnergonUtil.format(v));

        try{
            TransactionTypeEnum typeEnum = TransactionTypeEnum.getEnum(this.getTxType());
            switch (typeEnum){
                case TRANSACTION_VOTE_TICKET:
                    // 如果是投票交易，则解析投票参数信息
                    if(StringUtils.isNotBlank(txInfo)){
                        TxInfo info = JSON.parseObject(txInfo,TxInfo.class);
                        TxInfo.Parameter parameter = info.getParameters();
                        if (parameter!=null&&parameter.getPrice()!=null){
                            this.setTicketPrice(Convert.fromWei(BigDecimal.valueOf(parameter.getPrice().longValue()), Convert.Unit.ETHER));
                        }
                        if (parameter!=null&&parameter.getCount()!=null){
                            this.setVoteCount(parameter.getCount());
                        }
                        if(parameter!=null&&parameter.getNodeId()!=null){
                            this.setNodeId(parameter.getNodeId());
                        }
                    }
                    break;
                case TRANSACTION_CANDIDATE_DEPOSIT:
                case TRANSACTION_CANDIDATE_WITHDRAW:
                case TRANSACTION_CANDIDATE_APPLY_WITHDRAW:
                    if(StringUtils.isNotBlank(this.value)){
                        Double dep = Double.valueOf(this.value);
                        this.setDeposit(BigDecimal.valueOf(dep));
                    }else {
                        this.setDeposit(BigDecimal.ZERO);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
