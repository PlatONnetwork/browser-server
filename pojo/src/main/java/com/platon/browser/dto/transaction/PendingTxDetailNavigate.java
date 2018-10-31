package com.platon.browser.dto.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PendingTxDetailNavigate extends PendingTxDetail {
    // 是否最后一条
    private boolean last;
}
