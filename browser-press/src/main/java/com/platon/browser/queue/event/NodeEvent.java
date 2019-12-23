package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Node;

import java.util.List;

public class NodeEvent {
    private List<Node> nodeList;

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> transactionList) {
        this.nodeList = nodeList;
    }
}
