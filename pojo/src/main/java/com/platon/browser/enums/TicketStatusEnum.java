package com.platon.browser.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum TicketStatusEnum {
    NORMAL(1,"正常"),
    SELECTED(2,"选中"),
    OFF_LIST(3,"掉榜"),
    EXPIRED(4,"过期");

    public Integer status;
    public String desc;

    TicketStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    private static Map<Integer, TicketStatusEnum> map = new HashMap<>();
    static {
        Arrays.asList(TicketStatusEnum.values()).forEach(typeEnum->map.put(typeEnum.status,typeEnum));
    }

    public static TicketStatusEnum getEnum(Integer status){
        return map.get(status);
    }
}
