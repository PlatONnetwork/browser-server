package com.platon.browser.complement.cache;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class NodeItem {
    private String nodeId;
    private String nodeName;
}
