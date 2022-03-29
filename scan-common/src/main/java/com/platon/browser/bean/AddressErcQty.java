package com.platon.browser.bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressErcQty {

    /**
     * 地址
     */
    private String address;

    /**
     * token erc20交易数
     */
    private long erc20TxQty;

    /**
     * token erc721交易数
     */
    private long erc721TxQty;

}
