package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.NodeOpt;
import lombok.Data;

import java.util.List;
@Data
public class NodeOptEvent {
    private List<NodeOpt> nodeOptList;
}
