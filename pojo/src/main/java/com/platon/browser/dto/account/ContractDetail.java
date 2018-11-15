package com.platon.browser.dto.account;

import com.platon.browser.dto.transaction.AccTransactionItem;
import lombok.Data;

import java.util.List;

@Data
public class ContractDetail {
    private String balance;
    private int tradeCount;
    private String developer;
    private int ownerCount;
    private List<AccTransactionItem> trades;
}
