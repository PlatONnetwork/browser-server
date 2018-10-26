package com.platon.browser.service;

import com.platon.browser.common.dto.account.AccountDetail;
import com.platon.browser.common.req.account.AccountDetailReq;

public interface AccountService {
    AccountDetail getAccountDetail(AccountDetailReq req);
}
