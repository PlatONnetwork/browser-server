package com.platon.browser.dto.node;

import lombok.Data;

/**
 * 节点信息
 */
@Data
public class NodeInfo {
    private float longitude;
    private float latitude;
    private int nodeType;
    private int netState;
}
