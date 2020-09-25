package com.platon.browser.common.collection.dto;

import lombok.Data;

@Data
public class ComplementInfo {
    // 交易类型
    Integer type = null;
    Integer toType = null;
    // 合约代码
    String binCode = null;
    // 合约方法
    String method = null;
    // 合约类型
    Integer contractType = null;
    // tx info信息
    String info = "{}";
}