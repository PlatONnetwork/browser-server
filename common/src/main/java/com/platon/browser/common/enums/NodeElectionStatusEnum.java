package com.platon.browser.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum NodeElectionStatusEnum {
    // 1-候选前100名 2-出块中 3-验证节点 4-备选前100名
    CANDIDATE(1),VERIFY(3),ALTERNATIVE(4);
    public int code;
    NodeElectionStatusEnum(int code) {this.code = code;}
    private static Map<Integer, NodeElectionStatusEnum> map = new HashMap<>();
    static {Arrays.asList(NodeElectionStatusEnum.values()).forEach(snum->map.put(snum.code,snum));}
    public static NodeElectionStatusEnum getEnum(int code){
        return map.get(code);
    }
}
