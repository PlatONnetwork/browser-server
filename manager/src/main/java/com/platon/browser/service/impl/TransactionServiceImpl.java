package com.platon.browser.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionListReq;
import com.platon.browser.service.TransactionService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private I18nUtil i18n;

    @Override
    public List<TransactionItem> getTransactionByBlockNumber(TransactionListReq req) {
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid())
                .andBlockNumberEqualTo(req.getHeight());
        condition.setOrderByClause("sequence desc");
        List<Transaction> transactions = transactionMapper.selectByExample(condition);

        long serverTime = System.currentTimeMillis();
        List<TransactionItem> transactionList = new ArrayList<>();
        transactions.forEach(transaction -> {
            TransactionItem bean = new TransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setBlockHeight(transaction.getBlockNumber());
            bean.setServerTime(serverTime);
            bean.setBlockTime(transaction.getTimestamp().getTime());
            transactionList.add(bean);
        });
        return transactionList;
    }


    private TransactionDetail loadDetail(TransactionDetailReq req){
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.TRANSACTION_ERROR_DUPLICATE));
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(),i18n.i(I18nEnum.TRANSACTION_ERROR_NOT_EXIST));
        }
        TransactionDetail transactionDetail = new TransactionDetail();
        TransactionWithBLOBs currentTran = transactions.get(0);
        BeanUtils.copyProperties(currentTran,transactionDetail);
        transactionDetail.setTxHash(currentTran.getHash());
        transactionDetail.setBlockHeight(currentTran.getBlockNumber());
        transactionDetail.setTimestamp(currentTran.getTimestamp().getTime());
        transactionDetail.setInputData(currentTran.getInput());
        return transactionDetail;
    }

    private void setupNavigateFlag(String chainId,TransactionDetail detail){
        /** 设置first和last标识 **/
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(chainId)
                .andSequenceLessThan(detail.getSequence());
        PageHelper.startPage(1,1);
        List<Transaction> transactionList = transactionMapper.selectByExample(condition);
//        long transactionCount = transactionMapper.countByExample(condition);
        if(transactionList.size()==0){
            detail.setFirst(true);
        }

        condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(chainId)
                .andSequenceGreaterThan(detail.getSequence());
        PageHelper.startPage(1,1);
        transactionList = transactionMapper.selectByExample(condition);
        if(transactionList.size()==0){
            detail.setLast(true);
        }
    }

    @Override
    public TransactionDetail getTransactionDetail(TransactionDetailReq req) {

        // 取得当前交易详情
        TransactionDetail transactionDetail = loadDetail(req);
        // 设置浏览前后标识
        setupNavigateFlag(req.getCid(),transactionDetail);

        return transactionDetail;
    }

    /**
     * 通过账户信息获取交易列表, 以太坊账户有两种类型：外部账户-钱包地址，内部账户-合约地址
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
            // 根据交易类型查询
            first.andTxTypeEqualTo(req.getTxType());
            second.andTxTypeEqualTo(req.getTxType());
        }
        if(req.getStartDate()!=null){
            // 根据交易生成起始时间查询
            first.andTimestampGreaterThanOrEqualTo(req.getStartDate());
            second.andTimestampGreaterThanOrEqualTo(req.getStartDate());
        }
        if(req.getEndDate()!=null){
            // 根据交易生成结束时间查询
            first.andTimestampLessThanOrEqualTo(req.getEndDate());
            second.andTimestampLessThanOrEqualTo(req.getEndDate());
        }
        condition.or(second);
        condition.setOrderByClause("timestamp desc");
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        return transactions;
    }

    /**
     * 上一个、下一个，浏览交易信息，交易可能跨区块
     * @param req
     * @return
     */
    @Override
    public TransactionDetail getTransactionDetailNavigate(TransactionDetailNavigateReq req) {

        // 取得当前交易详情
        TransactionDetailReq detailReq = new TransactionDetailReq();
        BeanUtils.copyProperties(req,detailReq);
        TransactionDetail currentDetail = loadDetail(detailReq);

        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria criteria = condition.createCriteria()
                .andChainIdEqualTo(req.getCid());
        switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())){
            case PREV:
                criteria.andSequenceLessThan(currentDetail.getSequence());
                condition.setOrderByClause("sequence desc");
                break;
            case NEXT:
                criteria.andSequenceGreaterThan(currentDetail.getSequence());
                condition.setOrderByClause("sequence asc");
                break;
        }

        PageHelper.startPage(1,1);
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.TRANSACTION_ERROR_NOT_EXIST));
        }

        TransactionWithBLOBs transaction = transactions.get(0);
        TransactionDetail transactionDetail = new TransactionDetail();
        BeanUtils.copyProperties(transaction,transactionDetail);
        transactionDetail.setTxHash(transaction.getHash());
        transactionDetail.setBlockHeight(transaction.getBlockNumber());
        transactionDetail.setInputData(transaction.getInput());
        transactionDetail.setTimestamp(transaction.getTimestamp().getTime());

        // 设置浏览前后标识
        setupNavigateFlag(req.getCid(),transactionDetail);

        return transactionDetail;
    }
}
