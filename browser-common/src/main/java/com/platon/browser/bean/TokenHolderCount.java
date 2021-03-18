package com.platon.browser.bean;

import lombok.Data;

@Data
public class TokenHolderCount {

    /**
     * 合约地址
     */
    private String tokenAddress;

    /**
     * token对应的持有人的数量
     */
    private Integer tokenHolderCount;

}
