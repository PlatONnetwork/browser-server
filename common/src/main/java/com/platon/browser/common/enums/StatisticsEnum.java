package com.platon.browser.common.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * \*
 * \* User: dongqile
 * \* Date: 2018/6/27
 * \* Time: 14:53
 * \
 */
public enum StatisticsEnum {
    reward_amount,profit_amount,verify_count,block_count,block_reward;

    private static Map<String, StatisticsEnum> map = new HashMap<>();
    static {
        Arrays.asList(StatisticsEnum.values()).forEach(snum->map.put(snum.name(),snum));
    }
    public static StatisticsEnum getEnum(String name){
        return map.get(name);
    }
}