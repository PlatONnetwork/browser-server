package com.platon.browser.service;

import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.req.account.AccountDetailReq;

import java.util.List;

public interface AccountService {
    List<AccTransactionItem> getTransactionList(AccountDetailReq req);
}
