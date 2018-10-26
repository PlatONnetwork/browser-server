package com.platon.browser.common.dto.node;

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
