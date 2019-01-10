package com.platon.browser.service.impl;

import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.PendingTxExample;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.PendingTxDetail;
import com.platon.browser.dto.transaction.PendingTxDetailNavigate;
import com.platon.browser.dto.transaction.PendingTxItem;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailNavigateReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.PendingTxListReq;
import com.platon.browser.service.PendingTxService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
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
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private I18nUtil i18n;

    @Override
    public List<PendingTxItem> getTransactionList(PendingTxListReq req) {
        PendingTxExample condition = new PendingTxExample();
        condition.setOrderByClause("timestamp desc");
        if(StringUtils.isBlank(req.getAddress())){
            condition.createCriteria().andChainIdEqualTo(req.getCid());
        }else {
            // 根据发送方或接收方地址筛选
            condition.createCriteria().andChainIdEqualTo(req.getCid()).andFromEqualTo(req.getAddress());
            PendingTxExample.Criteria criteria = condition.createCriteria().andChainIdEqualTo(req.getCid()).andToEqualTo(req.getAddress());
            condition.or(criteria);
        }

        List<PendingTx> pendingTxes = pendingTxMapper.selectByExample(condition);
        List<PendingTxItem> pendingTxList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        pendingTxes.forEach(transaction -> {
            PendingTxItem bean = new PendingTxItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setTimestamp(transaction.getTimestamp().getTime());
            bean.setServerTime(serverTime);
            pendingTxList.add(bean);
        });
        return pendingTxList;
    }

    @Override
    public PendingOrTransaction getDetail(PendingTxDetailReq req) {
        PendingOrTransaction pot = new PendingOrTransaction();
        // 先根据交易Hash查询交易表
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        long transactionCount = transactionMapper.countByExample(transactionExample);
        if(transactionCount>0){
            pot.setType("transaction");
            return pot;
        }

        PendingTxExample condition = new PendingTxExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        List<PendingTx> transactions = pendingTxMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.PENDING_ERROR_DUPLICATE));
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.PENDING_ERROR_NOT_EXIST));
        }
        PendingTxDetail pendingTxDetail = new PendingTxDetail();
        PendingTx transaction = transactions.get(0);
        BeanUtils.copyProperties(transaction,pendingTxDetail);
        pendingTxDetail.setTxHash(transaction.getHash());
        pendingTxDetail.setInputData(transaction.getInput());
        pendingTxDetail.setTimestamp(transaction.getTimestamp().getTime());
        pot.setType("pending");
        pot.setPending(pendingTxDetail);
        return pot;
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
        List<PendingTx> pendingTxes = pendingTxMapper.selectByExampleWithBLOBs(condition);
        return pendingTxes;
    }

    @Override
    public PendingTxDetailNavigate getPendingTxDetailNavigate(PendingTxDetailNavigateReq req) {
        req.setPageSize(1);
        switch (NavigateEnum.valueOf(req.getDirection().toUpperCase())){
            case PREV:
                req.setPageNo(req.getIndex()-1);
                break;
            case NEXT:
                req.setPageNo(req.getIndex()+1);
                break;
        }
        req.buildPage();
        PendingTxExample condition = new PendingTxExample();
        condition.setOrderByClause("timestamp desc");
        List<PendingTx> pendingTxes = pendingTxMapper.selectByExample(condition);
        if(pendingTxes.size()==0){
            logger.error("no more pending transactions");
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.PENDING_ERROR_NOT_EXIST));
        }

        PendingTx pendingTx = pendingTxes.get(0);
        PendingTxDetailNavigate pendingTxDetailNavigate = new PendingTxDetailNavigate();
        BeanUtils.copyProperties(pendingTx,pendingTxDetailNavigate);
        pendingTxDetailNavigate.setTxHash(pendingTx.getHash());
        pendingTxDetailNavigate.setInputData(pendingTx.getInput());
        pendingTxDetailNavigate.setTimestamp(pendingTx.getTimestamp().getTime());
        return pendingTxDetailNavigate;
    }
}
