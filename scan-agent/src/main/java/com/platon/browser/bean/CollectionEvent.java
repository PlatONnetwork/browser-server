package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CollectionEvent {

    /**
     * 链路ID
     */
    private String traceId;

    // 区块信息
    private Block block;

    // 交易列表
    private List<Transaction> transactions = new ArrayList<>();

    private EpochMessage epochMessage;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public EpochMessage getEpochMessage() {
        return epochMessage;
    }

    public void setEpochMessage(EpochMessage epochMessage) {
        this.epochMessage = epochMessage;
    }

    /**
     * 释放对象引用
     */
    public void releaseRef() {
        block = null;
        transactions = null;
        epochMessage = null;
    }

}
