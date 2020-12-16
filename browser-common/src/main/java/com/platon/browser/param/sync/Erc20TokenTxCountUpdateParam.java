package com.platon.browser.param.sync;

import lombok.Data;

/**
 * erc20_token表tx_count更新参数
 */
@Data
public class Erc20TokenTxCountUpdateParam {
    private String address;
    private Integer txCount;
}
