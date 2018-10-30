package com.platon.browser.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionDetailNavigate;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.TransactionErrorEnum;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
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
    @Autowired
    private BlockMapper blockMapper;

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

        // 计算区块确认数
        BlockExample blockExample = new BlockExample();
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        if(blockList.size()==0){
            transactionDetail.setConfirmNum(0l);
            return transactionDetail;
        }
        Block block = blockList.get(0);
        transactionDetail.setConfirmNum(block.getNumber()-transactionDetail.getBlockHeight());
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

    /**
     * 上一个、下一个，浏览交易信息，交易可能跨区块
     * @param req
     * @return
     */
    @Override
    public TransactionDetailNavigate getTransactionDetailNavigate(TransactionDetailNavigateReq req) {

        // 根据当前交易hash查出当前交易信息
        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo(req.getCid()).andHashEqualTo(req.getTxHash());
        List<Transaction> transactions = transactionMapper.selectByExample(condition);
        if (transactions.size()>1){
            logger.error("duplicate transaction: transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.DUPLICATE.desc);
        }
        if(transactions.size()==0){
            logger.error("invalid transaction hash {}",req.getTxHash());
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.NOT_EXIST.desc);
        }
        Transaction currTransaction = transactions.get(0);

        // 根据方向查询同一区块上一条或下一条交易
        condition = new TransactionExample();
        TransactionExample.Criteria criteria = condition.createCriteria()
                .andChainIdEqualTo(currTransaction.getChainId())
                .andBlockNumberEqualTo(currTransaction.getBlockNumber());
        NavigateEnum direction = NavigateEnum.valueOf(req.getDirection().toUpperCase());
        int step = 0;
        switch (direction){
            case PREV:
                step = -1;
                break;
            case NEXT:
                step = 1;
                break;
        }
        criteria.andTransactionIndexEqualTo(currTransaction.getTransactionIndex()+step);
        List<TransactionWithBLOBs> transactionList = transactionMapper.selectByExampleWithBLOBs(condition);
        if (transactionList.size()>1){
            // 同一区块出现多条交易索引相同的记录
            logger.error("duplicate transaction: transaction index {}",currTransaction.getTransactionIndex()+step);
            throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.DUPLICATE.desc);
        }

        TransactionDetailNavigate transactionDetailNavigate = new TransactionDetailNavigate();

        if(transactionList.size()==1){
            // 在当前区块找到一条交易记录
            TransactionWithBLOBs transaction = transactionList.get(0);
            BeanUtils.copyProperties(transaction,transactionDetailNavigate);
            transactionDetailNavigate.setTxHash(transaction.getHash());
            transactionDetailNavigate.setBlockHeight(transaction.getBlockNumber());
            transactionDetailNavigate.setInputData(transaction.getInput());
        }

        if(transactionList.size()==0){
            // 当前区块找不到，则需要跨块查找
            condition = new TransactionExample();
            criteria = condition.createCriteria().andChainIdEqualTo(currTransaction.getChainId());
            long blockNumber = 0;
            switch (direction){
                case PREV:
                    // 上一条，则拿上一个块的最后一条交易
                    blockNumber=currTransaction.getBlockNumber()-1;
                    criteria.andBlockNumberEqualTo(blockNumber);
                    condition.setOrderByClause("transaction_index desc");
                    break;
                case NEXT:
                    // 下一条，则取下一个块的第一条交易
                    blockNumber=currTransaction.getBlockNumber()+1;
                    criteria.andBlockNumberEqualTo(blockNumber);
                    condition.setOrderByClause("transaction_index asc");
                    break;
            }
            // 只取一条
            PageHelper.startPage(1,1);
            transactionList = transactionMapper.selectByExampleWithBLOBs(condition);
            if(transactionList.size()==0){
                logger.error("no transaction found in block: {}",blockNumber);
                throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.NOT_EXIST.desc);
            }
            TransactionWithBLOBs transaction = transactionList.get(0);
            BeanUtils.copyProperties(transaction,transactionDetailNavigate);
            transactionDetailNavigate.setTxHash(transaction.getHash());
            transactionDetailNavigate.setBlockHeight(transaction.getBlockNumber());
            transactionDetailNavigate.setInputData(transaction.getInput());
        }
        return transactionDetailNavigate;
    }
}
