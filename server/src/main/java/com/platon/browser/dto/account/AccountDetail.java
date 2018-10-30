package com.platon.browser.dto.account;

import com.platon.browser.dto.transaction.AccTransactionItem;
import lombok.Data;

import java.util.List;

@Data
public class AccountDetail {
    private String balance;
    private int tradeCount;
    private String votePledge;
    private int nodeCount;
    private List<AccTransactionItem> trades;
}
