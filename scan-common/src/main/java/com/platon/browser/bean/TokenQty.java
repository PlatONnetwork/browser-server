package com.platon.browser.bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenQty {

    /**
     * 合约地址
     */
    private String contract;

    /**
     * token交易总数=erc20交易数+erc721交易数
     */
    private long tokenTxQty;

    /**
     * token erc20交易数
     */
    private long erc20TxQty;

    /**
     * token erc721交易数
     */
    private long erc721TxQty;

}
