package com.platon.browser.dto.cache;

import com.platon.browser.dto.transaction.TransactionInfo;
import lombok.Data;

import java.util.List;

@Data
public class TransactionInit {
    private boolean changed=false; // 是否有改动
    private List<TransactionInfo> list;
}
