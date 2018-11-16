package com.platon.browser.dao.entity;

import lombok.Data;

@Data
public class TransactionPage extends PageParam {
    // 块高
    private Long height;
}
