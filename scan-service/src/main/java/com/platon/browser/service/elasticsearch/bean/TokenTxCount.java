package com.platon.browser.service.elasticsearch.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * token交易数信息
 */
@Data
public class TokenTxCount {
    // 在本token对应的所有地址的交易数量
    private Long tokenTxCount = 0L;
    // 与当前token相关的地址的交易数
    private Map<String,Long> tokenTxCountMap = new HashMap<>();
}
