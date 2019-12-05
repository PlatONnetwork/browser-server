package com.platon.browser.common.queue.collection.event;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;

import java.util.List;

public class CollectionEvent {
    // 区块信息
    private Block block;
    // 交易列表
    private List<Transaction> transactions;
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
}
