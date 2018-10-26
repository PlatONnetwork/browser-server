package com.platon.browser.service.impl;

import com.platon.browser.common.dto.account.AccountDetail;
import com.platon.browser.common.dto.transaction.AllTransactionList;
import com.platon.browser.common.req.account.AccountDetailReq;
import com.platon.browser.service.AccountService;
import com.platon.browser.service.AllTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Autowired
    private AllTransactionService allTransactionService;

    @Override
    public AccountDetail getAccountDetail(AccountDetailReq req) {
        AccountDetail accountDetail = new AccountDetail();
        List<AllTransactionList> transactions = allTransactionService.getTransactionList(req);
        accountDetail.setTrades(transactions);
        return accountDetail;
    }
}
