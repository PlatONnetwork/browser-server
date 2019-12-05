package com.platon.browser.common.queue.complement.event;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;

import java.util.List;

public class ComplementEvent {
    // 区块信息
    private Block block;
    // 交易列表
    private List<Transaction> transactions;
    // 交易列表
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
