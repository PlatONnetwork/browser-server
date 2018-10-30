package com.platon.browser.dto.node;

import lombok.Data;

/**
 * 节点信息
 */
@Data
public class NodeInfo {
    private String longitude;
    private String latitude;
    private int nodeType;
    private int netState;
}
