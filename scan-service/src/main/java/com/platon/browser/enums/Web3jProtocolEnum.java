package com.platon.browser.enums;

import lombok.Getter;

public enum Web3jProtocolEnum {
    WS("ws://"),HTTP("http://");
    @Getter
    private String head;
    Web3jProtocolEnum(String head){
        this.head = head;
    }
}
