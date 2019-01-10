package com.platon.browser.service.impl;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.service.AccountService;
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
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PendingTxService pendingTxService;

    @Override
    public List<AccTransactionItem> getTransactionList(AccountDetailReq req) {
        // 取已完成交易
        req.buildPage();
        List<TransactionWithBLOBs> transactions = transactionService.getList(req);
        long serverTime = System.currentTimeMillis();
        List<AccTransactionItem> accTransactionList = new ArrayList<>();
        transactions.forEach(transaction -> {
            AccTransactionItem bean = new AccTransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setServerTime(serverTime);
            // 交易生成的时间就是出块时间
            bean.setBlockTime(transaction.getTimestamp().getTime());
            accTransactionList.add(bean);
        });

        // 取待处理交易
        req.buildPage();
        List<PendingTx> pendingTxes = pendingTxService.getTransactionList(req);
        pendingTxes.forEach(pendingTx -> {
            AccTransactionItem bean = new AccTransactionItem();
            BeanUtils.copyProperties(pendingTx,bean);
            bean.setTxHash(pendingTx.getHash());
            bean.setServerTime(serverTime);
            bean.setTxReceiptStatus(-1); // 手动设置交易状态为pending
            bean.setActualTxCost("0");
            accTransactionList.add(bean);
        });

        // 按时间倒排
        Collections.sort(accTransactionList,(c1,c2)->{
            long t1 = c1.getTimestamp().getTime(),t2 = c2.getTimestamp().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });
        return accTransactionList;
    }
}
