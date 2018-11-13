package com.platon.browser.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionDetailNavigate;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.enums.BlockErrorEnum;
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

import java.util.*;

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
        condition.setOrderByClause("block_number desc,transaction_index desc");
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        List<TransactionItem> transactionList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();

        // 查询交易所属的区块信息
        Map<Long, Block> map = new HashMap<>();
        List<Long> blockNumberList = new ArrayList<>();
        if(transactions.size()>0){
            transactions.forEach(transaction -> blockNumberList.add(transaction.getBlockNumber()));
            BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(req.getCid())
                    .andNumberIn(blockNumberList);
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            blocks.forEach(block->map.put(block.getNumber(),block));
        }

        transactions.forEach(transaction -> {
            TransactionItem bean = new TransactionItem();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getHash());
            bean.setBlockHeight(transaction.getBlockNumber());
            bean.setServerTime(serverTime);
            Block block = map.get(transaction.getBlockNumber());
            if(block!=null){
                bean.setBlockTime(block.getTimestamp().getTime());
            }
            transactionList.add(bean);
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
        TransactionWithBLOBs transaction = transactions.get(0);
        BeanUtils.copyProperties(transaction,transactionDetail);
        transactionDetail.setTxHash(transaction.getHash());
        transactionDetail.setBlockHeight(transaction.getBlockNumber());
        transactionDetail.setTimestamp(transaction.getTimestamp().getTime());
        transactionDetail.setInputData(transaction.getInput());

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

        // 取最高区块，用于计算区块确认数
        BlockExample blockExample = new BlockExample();
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> blockList = blockMapper.selectByExample(blockExample);

        if(transactionList.size()==1){
            // 在当前区块找到一条交易记录
            TransactionWithBLOBs transaction = transactionList.get(0);
            BeanUtils.copyProperties(transaction,transactionDetailNavigate);
            transactionDetailNavigate.setTxHash(transaction.getHash());
            transactionDetailNavigate.setBlockHeight(transaction.getBlockNumber());
            transactionDetailNavigate.setInputData(transaction.getInput());
            transactionDetailNavigate.setTimestamp(transaction.getTimestamp().getTime());
        }

        if(transactionList.size()==0){
            // 需要先查询下一个交易记录的区块
            /** 取下一条交易记录所在的区块号: blockNumber **/
            // 下一个区块的块号
            long currentBlockNumber = currTransaction.getBlockNumber();
            blockExample = new BlockExample();
            BlockExample.Criteria blockCriteria = blockExample.createCriteria()
                    .andChainIdEqualTo(currTransaction.getChainId());
            switch (direction){
                case PREV:
                    // 向前，则查询快高小于当前块且交易数大于0的上一个块
                    blockCriteria.andNumberLessThan(currentBlockNumber).andTransactionNumberGreaterThan(0);
                    blockExample.setOrderByClause("number desc");
                    break;
                case NEXT:
                    // 向后，则查询快高大于当前块且交易数大于0的下一个块
                    blockCriteria.andNumberGreaterThan(currentBlockNumber).andTransactionNumberGreaterThan(0);
                    blockExample.setOrderByClause("number asc");
                    break;
            }
            PageHelper.startPage(1,1);
            blockList = blockMapper.selectByExample(blockExample);
            if(blockList.size()==0){
                // 查询无结果，则认为已经没有交易记录
                logger.error("no more transaction");
                throw new BusinessException(RetEnum.RET_FAIL.getCode(), BlockErrorEnum.NOT_EXIST.desc);
            }
            Block block = blockList.get(0);
            long nextBlockNumber = block.getNumber();

            // 当前区块找不到，则需要跨块查找
            condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(currTransaction.getChainId()).andBlockNumberEqualTo(nextBlockNumber);
            switch (direction){
                case PREV:
                    // 上一条，则拿上一个块的最后一条交易
                    condition.setOrderByClause("transaction_index desc");
                    break;
                case NEXT:
                    // 下一条，则取下一个块的第一条交易
                    condition.setOrderByClause("transaction_index asc");
                    break;
            }
            // 只取一条
            PageHelper.startPage(1,1);
            transactionList = transactionMapper.selectByExampleWithBLOBs(condition);
            if(transactionList.size()==0){
                logger.error("no transaction found in block: {}",nextBlockNumber);
                throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.NOT_EXIST.desc);
            }
            TransactionWithBLOBs transaction = transactionList.get(0);
            BeanUtils.copyProperties(transaction,transactionDetailNavigate);
            transactionDetailNavigate.setTxHash(transaction.getHash());
            transactionDetailNavigate.setBlockHeight(transaction.getBlockNumber());
            transactionDetailNavigate.setInputData(transaction.getInput());
            transactionDetailNavigate.setTimestamp(transaction.getTimestamp().getTime());
        }

        // 计算区块确认数
        if(blockList.size()==0){
            transactionDetailNavigate.setConfirmNum(0l);
            return transactionDetailNavigate;
        }
        Block block = blockList.get(0);
        transactionDetailNavigate.setConfirmNum(block.getNumber()-transactionDetailNavigate.getBlockHeight());
        return transactionDetailNavigate;
    }
}
