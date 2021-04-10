package com.platon.browser.param.sync;

import lombok.Data;

/**
 * 地址表tokenQty更新参数
 */
@Data
public class AddressTokenQtyUpdateParam {
    private String address;
    private Integer tokenQty;
}
