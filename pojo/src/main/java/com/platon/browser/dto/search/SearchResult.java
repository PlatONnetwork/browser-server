package com.platon.browser.dto.search;

import lombok.Data;

@Data
public class SearchResult<T> {
    /**
     * 区块block，交易transaction，节点node,合约contract,账户account
     */
    private String type;
    /**
     * 具体的类型对应的详情的结构
     */
    private T struct;
}
