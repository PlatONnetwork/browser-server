package com.platon.browser.persistence.queue.event;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;

import java.util.List;

/**
 * 区块&交易持久化事件
 */
public class PersistenceEvent {
    // 区块
    private Block block;
    // 交易
    private List<Transaction> transactions;
    // 节点操作记录
    private List<NodeOpt> nodeOpts;

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

    public List<NodeOpt> getNodeOpts() {
        return nodeOpts;
    }

    public void setNodeOpts(List<NodeOpt> nodeOpts) {
        this.nodeOpts = nodeOpts;
    }
}
