package com.platon.browser.common.dto;

import lombok.Data;

@Data
public class NodeInfo {
    private String longitude;
    private String latitude;
    private int nodeType;
    private int netState;
}
