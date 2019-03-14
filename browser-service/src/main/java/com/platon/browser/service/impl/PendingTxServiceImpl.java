package com.platon.browser.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.PendingTxExample;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.PendingTxDetail;
import com.platon.browser.dto.transaction.PendingTxItem;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.PendingTxPageReq;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.PendingTxService;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PendingTxServiceImpl implements PendingTxService {

    private final Logger logger = LoggerFactory.getLogger(PendingTxServiceImpl.class);

    @Autowired
    private PendingTxMapper pendingTxMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private NodeService nodeService;

    @Override
    public RespPage<PendingTxItem> getTransactionList(PendingTxPageReq req) {
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
        Page page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<PendingTx> pendingTxes = pendingTxMapper.selectByExample(condition);
        List<PendingTxItem> data = new ArrayList<>();
        pendingTxes.forEach(initData -> {
            PendingTxItem bean = new PendingTxItem();
            bean.init(initData);
            data.add(bean);
        });

        RespPage<PendingTxItem> returnData = new RespPage<>();
        returnData.init(page,data);
        return returnData;
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
        PendingTxDetail returnData = new PendingTxDetail();
        PendingTx initData = transactions.get(0);
        returnData.init(initData);
        // 获取节点名称
        if(StringUtils.isNotBlank(returnData.getNodeId())){
            // 查询节点名称
            Map<String,String> nameMap = nodeService.getNodeNameMap(req.getCid(),Arrays.asList(returnData.getNodeId()));
            returnData.setNodeName(nameMap.get(returnData.getNodeId()));
        }

        pot.setType("pending");
        pot.setPending(returnData);
        return pot;
    }

    /**
     * 通过账户信息获取待处理交易列表
     * @param req
     * @return
     */
    @Override
    public List<PendingTx> getList(AddressDetailReq req) {
        PendingTxExample condition = new PendingTxExample();
        PendingTxExample.Criteria first = condition.createCriteria().andChainIdEqualTo(req.getCid())
                .andFromEqualTo(req.getAddress());
        PendingTxExample.Criteria second = condition.createCriteria()
                .andChainIdEqualTo(req.getCid())
                .andToEqualTo(req.getAddress());
        if(StringUtils.isNotBlank(req.getTxType())){
            // 根据交易类型查询
            if(req.getTxType().contains(",")){
                String [] txTypes = req.getTxType().split(",");
                List<String> txTypesList = Arrays.asList(txTypes);
                first.andTxTypeIn(txTypesList);
                second.andTxTypeIn(txTypesList);
            }else{
                first.andTxTypeEqualTo(req.getTxType());
                second.andTxTypeEqualTo(req.getTxType());
            }
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

}
