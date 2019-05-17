package com.platon.browser.service.app;

import com.platon.browser.dto.app.transaction.TransactionDto;
import com.platon.browser.req.app.AppTransactionListReq;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/17 10:03
 * @Description:
 */
public interface AppTransactionService {
    public List<TransactionDto> list(String chainId, AppTransactionListReq req);
}
