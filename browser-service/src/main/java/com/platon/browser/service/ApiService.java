package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionVoteReq;
import com.platon.browser.dto.transaction.VoteInfo;
import com.platon.browser.dto.transaction.VoteSummary;
import com.platon.browser.dto.transaction.VoteTransaction;
import com.platon.browser.req.transaction.TicketCountByTxHashReq;

import java.util.List;
import java.util.Map;

/*
*
* User: dongqile
* Date: 2019/3/19
* Time: 11:41
*/
public interface ApiService {
    List<VoteSummary> getVoteSummary(List<String> addressList, String chainId);
    RespPage<VoteTransaction> getVoteTransaction(TransactionVoteReq req);
    Map<String,Integer> getCandidateTicketCount(List<String> nodeIds, String chainId);
    RespPage<VoteInfo> getTicketCountByTxHash(TicketCountByTxHashReq req);
}
