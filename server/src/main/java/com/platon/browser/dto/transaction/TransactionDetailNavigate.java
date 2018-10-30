package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class TransactionDetailNavigate extends TransactionDetail {
    // 是否最后一条
    private boolean last;
}
