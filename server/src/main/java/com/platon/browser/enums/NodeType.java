package com.platon.browser.enums;

public enum NodeType {
    CONSENSUS(1,"共识节点"),
    NON_CONSENSUS(2,"非共识节点");
    public int code;
    public String desc;

    NodeType ( int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
