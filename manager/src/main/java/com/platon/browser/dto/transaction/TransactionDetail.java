package com.platon.browser.dto.transaction;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.ticket.TxInfo;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.util.EnergonUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class TransactionDetail {
    private String txHash;
    private long timestamp;
    private int txReceiptStatus;
    private Long blockHeight;
    private String from;
    private String to;
    private String txType;
    private String value;
    private String actualTxCost;
    private String energonLimit;
    private String energonUsed;
    private String energonPrice;
    private String priceInE;
    private String priceInEnergon;
    private String inputData;
    private long expectTime;
    private String failReason;
    private long confirmNum;
    private String receiveType;
    private String txInfo;
    private String nodeId;
    private Integer voteCount;
    // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
    @JsonIgnore
    private Long sequence;

    public void init(TransactionWithBLOBs initData) {
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setBlockHeight(initData.getBlockNumber());
        this.setTimestamp(initData.getTimestamp().getTime());
        this.setInputData(initData.getInput());
        BigDecimal v=Convert.fromWei(initData.getActualTxCost(), Convert.Unit.ETHER);
        this.setActualTxCost(EnergonUtil.format(v));
        v = Convert.fromWei(initData.getValue(), Convert.Unit.ETHER);
        this.setValue(EnergonUtil.format(v));
        this.setPriceInE(initData.getEnergonPrice());
        v = Convert.fromWei(initData.getEnergonPrice(), Convert.Unit.ETHER);
        this.setPriceInEnergon(EnergonUtil.format(v));

        try {
            TransactionTypeEnum typeEnum = TransactionTypeEnum.getEnum(txType);
            switch (typeEnum){
                case TRANSACTION_VOTE_TICKET:
                    if(StringUtils.isNotBlank(txInfo)){
                        TxInfo info = JSON.parseObject(txInfo,TxInfo.class);
                        TxInfo.Parameter parameter = info.getParameters();
                        if(parameter!=null){
                            this.setNodeId(parameter.getNodeId());
                            this.setVoteCount(parameter.getCount());
                        }
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
