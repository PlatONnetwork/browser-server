package com.platon.browser.common.dto.agent;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:18
 */
public class FailTransactionDto {

    /**
     * 交易hash
     */
    private String hash;

    /**
     * 失败交易原因
     */
    private String failReason;


    public String getHash () {
        return hash;
    }

    public void setHash ( String hash ) {
        this.hash = hash;
    }

    public String getFailReason () {
        return failReason;
    }

    public void setFailReason ( String failReason ) {
        this.failReason = failReason;
    }
}