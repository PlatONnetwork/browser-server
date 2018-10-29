package com.platon.browser.service.impl;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.dto.transaction.ConTransactionItem;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.service.ContractService;
import com.platon.browser.service.PendingTxService;
import com.platon.browser.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    private final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PendingTxService pendingTxService;

    @Override
    public ContractDetail getContractDetail(ContractDetailReq req) {
        List<ConTransactionItem> contractList = getContractList(req);

        ContractDetail contractDetail = new ContractDetail();

        contractDetail.setTrades(contractList);

        return contractDetail;
    }

    @Override
    public List<ConTransactionItem> getContractList(ContractDetailReq req) {
        req.buildPage();
        List<TransactionWithBLOBs> transactions = transactionService.getContractList(req);
        req.buildPage();
        List<PendingTx> pendingTxes = pendingTxService.getContractList(req);
        long serverTime = System.currentTimeMillis();
        List<ConTransactionItem> conTransactionList = new ArrayList<>();
        transactions.forEach(transaction -> {
            ConTransactionItem bean = new ConTransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setServerTime(serverTime);
            bean.setBlockTime(transaction.getTimestamp().getTime());
            conTransactionList.add(bean);
        });

        pendingTxes.forEach(pendingTx -> {
            ConTransactionItem bean = new ConTransactionItem();
            BeanUtils.copyProperties(pendingTx,bean);
            bean.setTxHash(pendingTx.getHash());
            bean.setServerTime(serverTime);
            bean.setBlockTime(pendingTx.getTimestamp().getTime());
            conTransactionList.add(bean);
        });

        // 按时间倒排
        Collections.sort(conTransactionList,(e1,e2)->{
            long t1 = e1.getCreateTime().getTime(),t2 = e2.getCreateTime().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });

        return conTransactionList;
    }


}
