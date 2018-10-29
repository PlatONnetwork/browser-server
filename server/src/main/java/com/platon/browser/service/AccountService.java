package com.platon.browser.service;

import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.req.account.AccountDetailReq;

public interface AccountService {
    AccountDetail getAccountDetail(AccountDetailReq req);
}
