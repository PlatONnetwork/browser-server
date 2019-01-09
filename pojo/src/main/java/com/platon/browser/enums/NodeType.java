package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum NodeType {
    CONSENSUS(1,"共识节点"),
    NON_CONSENSUS(2,"非共识节点");
    public int code;
    public String desc;

    NodeType ( int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static Map<Integer, NodeType> map = new HashMap<>();
    static {
        Arrays.asList(NodeType.values()).forEach(typeEnum->map.put(typeEnum.code,typeEnum));
    }

    public static NodeType getEnum(Integer code){
        return map.get(code);
    }
}
