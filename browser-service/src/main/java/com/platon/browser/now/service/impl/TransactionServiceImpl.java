package com.platon.browser.now.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.service.NodeService;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.service.cache.TransactionCacheService;
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
    @Autowired
    private TransactionCacheService transactionCacheService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private BlockMapper blockMapper;

/*    @Override
    public RespPage <TransactionListItem> getPage( TransactionPageReq req) {
        RespPage<TransactionListItem> returnData = transactionCacheService.getTransactionPage(req.getCid(),req.getPageNo(),req.getPageSize());
        return returnData;
    }

    @Override
    public RespPage<TransactionListItem> getPageByBlockNumber(TransactionPageReq req) {
        TransactionExample condition = new TransactionExample();
        condition.createCriteria()
                .andBlockNumberEqualTo(req.getHeight());
        condition.setOrderByClause("sequence desc");

        Page page = PageHelper.startPage(req.getPageNo(),req.getPageSize());
        List<Transaction> transactions = transactionMapper.selectByExample(condition);
        List<TransactionListItem> data = new ArrayList<>();
        transactions.forEach(initData -> {
            TransactionListItem bean = new TransactionListItem();
            bean.init(initData);
            data.add(bean);
        });
        RespPage<TransactionListItem> returnData = new RespPage<>();
        returnData.init(page,data);
        return returnData;
    }*/

    private TransactionDetail loadDetail(TransactionDetailReq req){
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andHashEqualTo(req.getTxHash());
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), i18n.i(I18nEnum.TRANSACTION_ERROR_DUPLICATE));
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(),i18n.i(I18nEnum.TRANSACTION_ERROR_NOT_EXIST));
        }
        TransactionDetail returnData = new TransactionDetail();
        TransactionWithBLOBs initData = transactions.get(0);
        returnData.init(initData);

        returnData.setNodeName("Unknown");
        String nodeId=returnData.getNodeId();
        if(StringUtils.isBlank(nodeId)){
            // 如果从txInfo中取不到节点ID，则从区块中取
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andNumberEqualTo(initData.getBlockNumber());
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            if(blocks.size()>0){
                Block block = blocks.get(0);
                if(StringUtils.isNotBlank(block.getNodeId())){
                    nodeId=block.getNodeId();
                    returnData.setNodeId(nodeId);
                }
            }
        }

        if(StringUtils.isNotBlank(nodeId)){
            // 查询节点名称
            Map<String,String> nameMap = nodeService.getNodeNameMap(req.getCid(),Arrays.asList(returnData.getNodeId()));
            returnData.setNodeName(nameMap.get(returnData.getNodeId()));
        }

        return returnData;
    }

    private void setupNavigateFlag(TransactionDetail detail){
        /** 设置first和last标识 **/
        TransactionExample condition = new TransactionExample();
        condition.createCriteria()
                .andSequenceLessThan(detail.getSequence());
        PageHelper.startPage(1,1);
        List<Transaction> transactionList = transactionMapper.selectByExample(condition);
//        long transactionCount = transactionMapper.countByExample(condition);
        if(transactionList.size()==0){
            detail.setFirst(true);
        }

        condition = new TransactionExample();
        condition.createCriteria()
                .andSequenceGreaterThan(detail.getSequence());
        PageHelper.startPage(1,1);
        transactionList = transactionMapper.selectByExample(condition);
        if(transactionList.size()==0){
            detail.setLast(true);
        }
    }

    @Override
    public TransactionDetail getDetail(TransactionDetailReq req) {
        // 取得当前交易详情
        TransactionDetail transactionDetail = loadDetail(req);
        return transactionDetail;
    }

    /**
     * 通过账户信息获取交易列表, 以太坊账户有两种类型：外部账户-钱包地址，内部账户-合约地址
     * @param req
     * @return
     */
    @Override
    public List<TransactionWithBLOBs> getList(AddressDetailReq req) {
        TransactionExample condition = new TransactionExample();
        TransactionExample.Criteria first = condition.createCriteria()
                .andFromEqualTo(req.getAddress());
        TransactionExample.Criteria second = condition.createCriteria()
                .andToEqualTo(req.getAddress());
        if(req.getTxTypes().size()>0){
            // 根据交易类型查询
            if(req.getTxTypes().size()>1){
                first.andTxTypeIn(req.getTxTypes());
                second.andTxTypeIn(req.getTxTypes());
            }else{
                first.andTxTypeEqualTo(req.getTxTypes().get(0));
                second.andTxTypeEqualTo(req.getTxTypes().get(0));
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
        TransactionExample.Criteria criteria = condition.createCriteria();
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

        TransactionWithBLOBs initData = transactions.get(0);
        TransactionDetail returnData = new TransactionDetail();
        returnData.init(initData);
        // 设置浏览前后标识
        setupNavigateFlag(returnData);

        returnData.setNodeName("Unknown");
        String nodeId=returnData.getNodeId();
        if(StringUtils.isBlank(nodeId)){
            // 如果从txInfo中取不到节点ID，则从区块中取
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andNumberEqualTo(initData.getBlockNumber());
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            if(blocks.size()>0){
                Block block = blocks.get(0);
                if(StringUtils.isNotBlank(block.getNodeId())){
                    nodeId=block.getNodeId();
                    returnData.setNodeId(nodeId);
                }
            }
        }

        if(StringUtils.isNotBlank(nodeId)){
            // 查询节点名称
            Map<String,String> nameMap = nodeService.getNodeNameMap(req.getCid(),Arrays.asList(returnData.getNodeId()));
            returnData.setNodeName(nameMap.get(returnData.getNodeId()));
        }

        return returnData;
    }

    @Override
    public void clearCache(String chainId) {
        transactionCacheService.clearTransactionCache(chainId);
    }

    @Override
    public void updateCache(String chainId,Set<Transaction> data) {
        transactionCacheService.updateTransactionCache(chainId,data);
    }

}
