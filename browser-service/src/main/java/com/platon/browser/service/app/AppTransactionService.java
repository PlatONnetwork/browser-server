package com.platon.browser.service.app;

import com.platon.browser.dto.app.transaction.AppTransactionDto;
import com.platon.browser.dto.app.transaction.AppVoteTransactionDto;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.req.app.AppTransactionListVoteReq;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/17 10:03
 * @Description:
 */
public interface AppTransactionService {
    List<AppTransactionDto> list(String chainId, AppTransactionListReq req);

    List<AppVoteTransactionDto> listVote(String chainId, AppTransactionListVoteReq req);
}
