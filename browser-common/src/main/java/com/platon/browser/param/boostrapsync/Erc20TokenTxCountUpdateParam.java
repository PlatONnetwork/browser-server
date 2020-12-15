package com.platon.browser.param.boostrapsync;

import lombok.Data;

/**
 * erc20_token表tx_count更新参数
 */
@Data
public class Erc20TokenTxCountUpdateParam {
    private String address;
    private Integer txCount;
}
