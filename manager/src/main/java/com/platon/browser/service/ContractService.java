package com.platon.browser.service;

import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.transaction.ConTransactionItem;
import com.platon.browser.req.account.ContractDetailReq;

import java.util.List;

public interface ContractService {
    ContractDetail getContractDetail(ContractDetailReq req);

    List<ConTransactionItem> getContractList(ContractDetailReq req);
}
