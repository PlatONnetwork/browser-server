package com.platon.browser.service.impl;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.account.AccountDetail;
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
    public AccountDetail getAccountDetail(AccountDetailReq req) {
        List<AccTransactionItem> accTransactionList = getTransactionList(req);

        AccountDetail accountDetail = new AccountDetail();

        // 大于20，则取前20条数据返回
        if(accTransactionList.size()>20){
            accountDetail.setTrades(accTransactionList.subList(0,20));
        }else{
            accountDetail.setTrades(accTransactionList);
        }
        return accountDetail;
    }

    @Override
    public List<AccTransactionItem> getTransactionList(AccountDetailReq req) {
        req.buildPage();
        List<TransactionWithBLOBs> transactions = transactionService.getTransactionList(req);
        req.buildPage();
        List<PendingTx> pendingTxes = pendingTxService.getTransactionList(req);
        long serverTime = System.currentTimeMillis();
        List<AccTransactionItem> accTransactionList = new ArrayList<>();
        transactions.forEach(transaction -> {
            AccTransactionItem bean = new AccTransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setServerTime(serverTime);
            bean.setBlockTime(transaction.getTimestamp().getTime());
            accTransactionList.add(bean);
        });

        pendingTxes.forEach(pendingTx -> {
            AccTransactionItem bean = new AccTransactionItem();
            BeanUtils.copyProperties(pendingTx,bean);
            bean.setTxHash(pendingTx.getHash());
            bean.setServerTime(serverTime);
            bean.setBlockTime(pendingTx.getTimestamp().getTime());
            bean.setTxReceiptStatus(-1); // 手动设置交易状态为pending
            accTransactionList.add(bean);
        });

        // 按时间倒排
        Collections.sort(accTransactionList,(e1,e2)->{
            long t1 = e1.getCreateTime().getTime(),t2 = e2.getCreateTime().getTime();
            if(t1<t2) return 1;
            if(t1>t2) return -1;
            return 0;
        });

        return accTransactionList;
    }
}
