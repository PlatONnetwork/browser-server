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
    // 虚拟交易列表(通过普通合约调用PPOS操作，需要调用特殊节点数据查询接口获取信息，并构造虚拟交易)
    private List<Transaction> virtualTransactions;
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
    public void releaseRef(){
        block=null;
        transactions=null;
        epochMessage=null;
    }
}
