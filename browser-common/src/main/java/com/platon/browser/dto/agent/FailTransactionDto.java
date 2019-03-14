package com.platon.browser.dto.agent;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:18
 */
@Data
public class FailTransactionDto {
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 失败交易原因
     */
    private String failReason;
}