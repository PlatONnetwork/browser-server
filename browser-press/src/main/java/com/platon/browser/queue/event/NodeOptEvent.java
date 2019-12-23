package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.NodeOpt;

import java.util.List;

public class NodeOptEvent {
    private List<NodeOpt> nodeOptList;

    public List<NodeOpt> getNodeOptList() {
        return nodeOptList;
    }

    public void setNodeOptList(List<NodeOpt> nodeOptList) {
        this.nodeOptList = nodeOptList;
    }
}
