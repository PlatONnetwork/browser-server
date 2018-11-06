package com.platon.browser.dto.query;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2018/11/1
 * Time: 18:00
 */
@Data
public class Query {
    /**
     * 区块block，交易transaction，节点node,合约contract,账户account
     */
    private String type;

    /**
     * 具体的类型对应的详情的结构
     */
    private Object struct;



}