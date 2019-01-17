package com.platon.browser.dto.account;

import com.platon.browser.dto.transaction.AccTransactionItem;
import lombok.Data;

import java.util.List;
@Data
public class AddressDetail {
    protected String balance;
    protected int tradeCount;
    protected List<AccTransactionItem> trades;
}
