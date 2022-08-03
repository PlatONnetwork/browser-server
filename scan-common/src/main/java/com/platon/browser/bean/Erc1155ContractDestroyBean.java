package com.platon.browser.bean;

import lombok.Data;

@Data
public class Erc1155ContractDestroyBean {

    /**
     * 合约地址
     */
    private String tokenAddress;

    /**
     * 合约id
     */
    private String tokenId;

    /**
     * 合约地址
     */
    private String address;

    /**
     * 销毁的块高
     */
    private Long contractDestroyBlock;

}
