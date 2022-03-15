package com.platon.browser.bean;

import lombok.Data;

@Data
public class Erc1155ContractDestroyBalanceVO {

    /**
     * 合约
     */
    private String tokenAddress;

    /**
     * 持有者
     */
    private String owner;

    /**
     * 数量
     */
    private Integer num;

}
