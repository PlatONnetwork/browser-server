package com.platon.browser.common.dto.mq;

import lombok.Data;

@Data
public class Message {
    private String chainId;
    private String type;
    private String struct;
}
