package com.platon.browser.service;

import com.platon.browser.dto.transaction.TransactionVoteReq;
import com.platon.browser.dto.transaction.VoteInfo;
import com.platon.browser.dto.transaction.VoteSummary;
import com.platon.browser.dto.transaction.VoteTransaction;
import com.platon.browser.req.transaction.TicketCountByTxHashReq;

import java.math.BigDecimal;
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
    Map<String,Integer> getCandidateTicketCount(List<String> nodeIds, String chainId);
    Map<String, BigDecimal> getIncome(String chainId, List<String> hashList);
    Map<String,Integer> getVailInfo(List<String> hashList,String chainId);
}
