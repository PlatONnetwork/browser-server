package com.platon.browser.queue.event;

import com.platon.browser.dao.entity.Node;
import lombok.Data;

import java.util.List;
@Data
public class NodeEvent {
    private List<Node> nodeList;
}
