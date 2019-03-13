package com.platon.browser.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionPage extends PageParam {
    // 块高
    private Long height;
}
