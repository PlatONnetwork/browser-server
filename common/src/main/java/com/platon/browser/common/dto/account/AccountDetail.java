package com.platon.browser.common.dto.account;

import com.platon.browser.common.dto.transaction.AllTransactionList;
import lombok.Data;

import java.util.List;

@Data
public class AccountDetail {
    private String balance;
    private int tradeCount;
    private String votePledge;
    private int nodeCount;
    private List<AllTransactionList> trades;
}
