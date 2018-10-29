package com.platon.browser.service.impl;

import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.enums.TransactionErrorEnum;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionListReq;
import com.platon.browser.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<TransactionItem> getTransactionList(TransactionListReq req) {
        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria criteria = condition.createCriteria()
                .andChainIdEqualTo(req.getCid());
        if(req.getHeight()!=null){
            // 根据块高筛选
            criteria.andBlockNumberEqualTo(req.getHeight());
        }
        condition.setOrderByClause("block_number desc");
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        List<TransactionItem> transactionList = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionItem tl = new TransactionItem();
            BeanUtils.copyProperties(transaction,tl);
            tl.setTxHash(transaction.getHash());
            tl.setBlockHeight(transaction.getBlockNumber());
            transactionList.add(tl);
        });
        return transactionList;
    }

    @Override
    public TransactionDetail getTransactionDetail(TransactionDetailReq req) {
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.DUPLICATE.desc);
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.NOT_EXIST.desc);
        }
        TransactionDetail transactionDetail = new TransactionDetail();
        Transaction transaction = transactions.get(0);
        BeanUtils.copyProperties(transaction,transactionDetail);
        transactionDetail.setTxHash(transaction.getHash());
        transactionDetail.setBlockHeight(transaction.getBlockNumber());
        return transactionDetail;
    }

    /**
     * 通过账户信息获取交易列表
     * @param req
     * @return
     */
    @Override
    public List<TransactionWithBLOBs> getTransactionList(AccountDetailReq req) {
        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria first = condition.createCriteria().andChainIdEqualTo(req.getCid())
                .andFromEqualTo(req.getAddress());
        TransactionExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andToEqualTo(req.getAddress());
        if(StringUtils.isNotBlank(req.getTxType())){
            first.andTxTypeEqualTo(req.getTxType());
            second.andTxTypeEqualTo(req.getTxType());
        }
        condition.or(second);
        condition.setOrderByClause("create_time desc");
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        return transactions;
    }

    @Override
    public List<TransactionWithBLOBs> getContractList(ContractDetailReq req) {
        String [] types = {TransactionTypeEnum.CONTRACT_CREATE.code,TransactionTypeEnum.TRANSACTION_EXECUTE.code};
        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria first = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andFromEqualTo(req.getAddress())
                .andTxTypeIn(Arrays.asList(types));
        TransactionExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andToEqualTo(req.getAddress())
                .andTxTypeIn(Arrays.asList(types));
        if(StringUtils.isNotBlank(req.getTxType())){
            first.andTxTypeEqualTo(req.getTxType());
            second.andTxTypeEqualTo(req.getTxType());
        }
        condition.or(second);
        condition.setOrderByClause("create_time desc");
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        return transactions;
    }
}
