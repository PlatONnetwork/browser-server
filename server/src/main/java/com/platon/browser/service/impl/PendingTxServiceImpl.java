package com.platon.browser.service.impl;

import com.platon.browser.common.dto.transaction.PendingTxDetail;
import com.platon.browser.common.dto.transaction.PendingTxList;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.enums.TransactionErrorEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.common.req.account.AccountDetailReq;
import com.platon.browser.common.req.transaction.PendingTxDetailReq;
import com.platon.browser.common.req.transaction.PendingTxListReq;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.PendingTxExample;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.service.PendingTxService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PendingTxServiceImpl implements PendingTxService {

    private final Logger logger = LoggerFactory.getLogger(PendingTxServiceImpl.class);

    @Autowired
    private PendingTxMapper pendingTxMapper;

    @Override
    public List<PendingTxList> getTransactionList(PendingTxListReq req) {
        PendingTxExample condition = new PendingTxExample();
        condition.setOrderByClause("timestamp desc");
        if(StringUtils.isBlank(req.getAddress())){
            condition.createCriteria().andChainIdEqualTo(req.getCid());
        }else {
            condition.createCriteria().andChainIdEqualTo(req.getCid()).andFromEqualTo(req.getAddress());
            PendingTxExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid()).andToEqualTo(req.getAddress());
            condition.or(criteria);
        }

        List<PendingTx> pendingTxes = pendingTxMapper.selectByExample(condition);
        List<PendingTxList> pendingTxList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        pendingTxes.forEach(transaction -> {
            PendingTxList pt = new PendingTxList();
            BeanUtils.copyProperties(transaction,pt);
            pt.setTxHash(transaction.getHash());
            pt.setDwellTime(serverTime-transaction.getTimestamp().getTime());
            pt.setServerTime(serverTime);
            pendingTxList.add(pt);
        });
        return pendingTxList;
    }

    @Override
    public PendingTxDetail getTransactionDetail(PendingTxDetailReq req) {
        PendingTxExample condition = new PendingTxExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        List<PendingTx> transactions = pendingTxMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.DUPLICATE.desc);
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(),TransactionErrorEnum.NOT_EXIST.desc);
        }
        PendingTxDetail transactionDetail = new PendingTxDetail();
        PendingTx transaction = transactions.get(0);
        BeanUtils.copyProperties(transaction,transactionDetail);
        transactionDetail.setTxHash(transaction.getHash());
        transactionDetail.setInputData(transaction.getInput());
        return transactionDetail;
    }

    /**
     * 通过账户信息获取待处理交易列表
     * @param req
     * @return
     */
    @Override
    public List<PendingTx> getTransactionList(AccountDetailReq req) {
        PendingTxExample condition = new PendingTxExample();
        PendingTxExample.Criteria first = condition.createCriteria().andChainIdEqualTo(req.getCid())
                .andFromEqualTo(req.getAddress());
        PendingTxExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andToEqualTo(req.getAddress());
        if(StringUtils.isNotBlank(req.getTxType())){
            first.andTxTypeEqualTo(req.getTxType());
            second.andTxTypeEqualTo(req.getTxType());
        }
        condition.or(second);
        condition.setOrderByClause("create_time desc");
        List<PendingTx> pendingTxes = pendingTxMapper.selectByExampleWithBLOBs(condition);
        return pendingTxes;
    }
}
