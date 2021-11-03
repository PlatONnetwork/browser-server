package com.platon.browser.bean;

import lombok.Data;

@Data
public class DestroyContract {

    /**
     * 合约地址
     */
    private String tokenAddress;

    /**
     * 销毁的块高
     */
    private long contractDestroyBlock;

    /**
     * 账户地址
     */
    private String account;

}
