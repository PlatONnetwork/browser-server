package com.platon.browser.dto.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionDetailNavigate extends TransactionDetail {
    // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
}
