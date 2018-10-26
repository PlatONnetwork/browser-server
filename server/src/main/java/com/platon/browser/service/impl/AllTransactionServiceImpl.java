package com.platon.browser.service.impl;

import com.platon.browser.common.dto.transaction.AllTransactionDetail;
import com.platon.browser.common.dto.transaction.AllTransactionList;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.enums.TransactionErrorEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.common.req.account.AccountDetailReq;
import com.platon.browser.common.req.transaction.AllTransactionDetailReq;
import com.platon.browser.common.req.transaction.AllTransactionListReq;
import com.platon.browser.dao.entity.AllTransactionExample;
import com.platon.browser.dao.entity.AllTransactionWithBLOBs;
import com.platon.browser.dao.mapper.AllTransactionMapper;
import com.platon.browser.service.AllTransactionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllTransactionServiceImpl implements AllTransactionService {

    private final Logger logger = LoggerFactory.getLogger(AllTransactionServiceImpl.class);

    @Autowired
    private AllTransactionMapper allTransactionMapper;

    @Override
    public List<AllTransactionList> getTransactionList(AllTransactionListReq req) {
        AllTransactionExample condition = new AllTransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid());
        condition.setOrderByClause("create_time desc");
        List<AllTransactionWithBLOBs> transactions = allTransactionMapper.selectByExampleWithBLOBs(condition);
        List<AllTransactionList> transactionList = new ArrayList<>();
        transactions.forEach(transaction -> {
            AllTransactionList bean = new AllTransactionList();
            BeanUtils.copyProperties(transaction,bean);
            transactionList.add(bean);
        });
        return transactionList;
    }

    /**
     * 通过账户地址获取交易信息
     * @param req
     * @return
     */
    @Override
    public List<AllTransactionList> getTransactionList(AccountDetailReq req) {
        AllTransactionExample condition = new AllTransactionExample();
        AllTransactionExample.Criteria first = condition.createCriteria().andChainIdEqualTo(req.getCid())
                .andFromEqualTo(req.getAddress());
        AllTransactionExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andToEqualTo(req.getAddress());
        if(StringUtils.isNotBlank(req.getTxType())){
            first.andTxTypeEqualTo(req.getTxType());
            second.andTxTypeEqualTo(req.getTxType());
        }
        condition.or(second);
        condition.setOrderByClause("create_time desc");
        List<AllTransactionWithBLOBs> transactions = allTransactionMapper.selectByExampleWithBLOBs(condition);
        List<AllTransactionList> transactionList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        transactions.forEach(transaction -> {
            AllTransactionList bean = new AllTransactionList();
            BeanUtils.copyProperties(transaction,bean);
            bean.setServerTime(serverTime);
            transactionList.add(bean);
        });
        return transactionList;
    }

    @Override
    public AllTransactionDetail getTransactionDetail(AllTransactionDetailReq req) {
        AllTransactionExample condition = new AllTransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andTxHashEqualTo(req.getTxHash());
        List<AllTransactionWithBLOBs> transactions = allTransactionMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.DUPLICATE.desc);
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(),TransactionErrorEnum.NOT_EXIST.desc);
        }
        AllTransactionDetail transactionDetail = new AllTransactionDetail();
        AllTransactionWithBLOBs transaction = transactions.get(0);
        BeanUtils.copyProperties(transaction,transactionDetail);
        return transactionDetail;
    }
}
