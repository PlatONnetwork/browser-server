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
import com.platon.browser.enums.NavigateEnum;
import com.platon.browser.enums.TransactionErrorEnum;
import com.platon.browser.req.account.AccountDetailReq;
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
        // 交易记录先根据区块号倒排，再根据交易索引倒排
        condition.setOrderByClause("block_number desc,transaction_index desc");
        List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(condition);
        List<TransactionItem> transactionList = new ArrayList<>();
        long serverTime = System.currentTimeMillis();

        // 查询交易所属的区块信息
        Map<Long, Block> map = new HashMap<>();
        List<Long> blockNumberList = new LinkedList<>();
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
            // 在当前区块找到前一条或下一条交易记录，记为A记录
            TransactionWithBLOBs transaction = transactionList.get(0);
            BeanUtils.copyProperties(transaction,transactionDetailNavigate);
            transactionDetailNavigate.setTxHash(transaction.getHash());
            transactionDetailNavigate.setBlockHeight(transaction.getBlockNumber());
            transactionDetailNavigate.setInputData(transaction.getInput());
            transactionDetailNavigate.setTimestamp(transaction.getTimestamp().getTime());

            // 查询与A记录同块的前一条或后一条记录，前后由step的值决定：-1向前，1向后
            condition = new TransactionExample();
            condition.createCriteria()
                    .andChainIdEqualTo(transaction.getChainId())
                    .andTransactionIndexEqualTo(transaction.getTransactionIndex()+step);
            List<Transaction> tmpTransactions = transactionMapper.selectByExample(condition);
            long prevOrNextCount = tmpTransactions.size();

            switch (direction){
                case PREV:
                    // 如果向前浏览，则A记录必定不是向后浏览的最后一条记录，只需要查看A记录前面是否还有记录或者它的前面是否存在有交易记录的块
                    if(prevOrNextCount==0){
                        // 同块无记录，则需要查看它的前面是否有存在交易记录的区块来决定
                        BlockExample blockExample = new BlockExample();
                        blockExample.createCriteria()
                                .andChainIdEqualTo(transaction.getChainId())
                                .andNumberLessThan(transaction.getBlockNumber())
                                .andTransactionNumberGreaterThan(0);
                        // 取一条记录，避免影响性能
                        PageHelper.startPage(1,1);
                        List<Block> tmpBlocks = blockMapper.selectByExample(blockExample);
                        long blockCount = tmpBlocks.size();
                        if(blockCount==0){
                            // 如果A记录前面不存在有交易记录的区块则认为A记录是第一条
                            transactionDetailNavigate.setFirst(true);
                        }
                    }
                    break;
                case NEXT:
                    // 如果向后浏览，则A记录必定不是向前浏览的第一条记录，只需要查看A记录后面是否还有记录或者它的后面是否存在有交易记录的块
                    if(prevOrNextCount==0){
                        // 同块无记录，则需要查看它的后面是否有存在交易记录的区块来决定
                        BlockExample blockExample = new BlockExample();
                        blockExample.createCriteria()
                                .andChainIdEqualTo(transaction.getChainId())
                                .andNumberGreaterThan(transaction.getBlockNumber())
                                .andTransactionNumberGreaterThan(0);
                        // 取一条记录，避免影响性能
                        PageHelper.startPage(1,1);
                        List<Block> tmpBlocks = blockMapper.selectByExample(blockExample);
                        long blockCount = tmpBlocks.size();
                        if(blockCount==0){
                            // 如果A记录后面不存在有交易记录的区块则认为A记录是最后一条
                            transactionDetailNavigate.setLast(true);
                        }
                    }
                    break;
            }
        }

        if(transactionList.size()==0){
            // 当前区块找不到，则需要跨块查找
            /** 第1步、获取前一个或下一个有交易记录的区块的区块号: prevOrNextBlockNumber **/
            long currentBlockNumber = currTransaction.getBlockNumber();
            BlockExample blockExample = new BlockExample();
            BlockExample.Criteria blockCriteria = blockExample.createCriteria()
                    .andChainIdEqualTo(currTransaction.getChainId());
            switch (direction){
                case PREV:
                    // 向前，则查询块高小于当前块高且交易数大于0的上一个块
                    blockCriteria.andNumberLessThan(currentBlockNumber).andTransactionNumberGreaterThan(0);
                    blockExample.setOrderByClause("number desc");
                    break;
                case NEXT:
                    // 向后，则查询块高大于当前块高且交易数大于0的下一个块
                    blockCriteria.andNumberGreaterThan(currentBlockNumber).andTransactionNumberGreaterThan(0);
                    blockExample.setOrderByClause("number asc");
                    break;
            }
            // 查询当前块的相邻的两个有交易记录的块
            PageHelper.startPage(1,1);
            List<Block> blockList = blockMapper.selectByExample(blockExample);
            if(blockList.size()==0){
                // 查询无结果，则认为向前或向后浏览已经没有交易记录，直接抛异常结束
                logger.error("no more transaction");
                throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.NOT_EXIST.desc);
            }

            // 取第一条记录，记为A区块，作为返回给客户端的数据
            Block block = blockList.get(0);
            long prevOrNextBlockNumber = block.getNumber();

            /** 第2步、根据块的交易数量设置first和last标识 **/
            if(block.getTransactionNumber()==1){
                /** 如果区块交易数等于1，则需要查询当前块前一个有交易记录的块来确定first标识，查询当前块的后一个有交易记录的块来确定last标识 **/
                // 查询前一个有交易记录的块
                blockExample = new BlockExample();
                blockExample.createCriteria()
                    .andChainIdEqualTo(currTransaction.getChainId())
                    .andNumberLessThan(prevOrNextBlockNumber)
                    .andTransactionNumberGreaterThan(0);
                // 取一条记录，避免影响性能
                PageHelper.startPage(1,1);
                List<Block> tmpBlocks = blockMapper.selectByExample(blockExample);
                long blockCount = tmpBlocks.size();
                if(blockCount==0){
                    // 如果前面不存在带有交易记录的块，则表示是第一条交易记录
                    transactionDetailNavigate.setFirst(true);
                }
                // 查询后一个有交易记录的块
                blockExample = new BlockExample();
                blockExample.createCriteria()
                        .andChainIdEqualTo(currTransaction.getChainId())
                        .andNumberGreaterThan(prevOrNextBlockNumber)
                        .andTransactionNumberGreaterThan(0);
                // 取一条记录，避免影响性能
                PageHelper.startPage(1,1);
                tmpBlocks = blockMapper.selectByExample(blockExample);
                blockCount = tmpBlocks.size();
                if(blockCount==0){
                    // 如果后面不存在带有交易记录的块，则表示是最后一条交易记录
                    transactionDetailNavigate.setLast(true);
                }
            }

            if(block.getTransactionNumber()>1){
                // 如果A区块交易数大于1
                switch (direction){
                    case PREV:
                        // 向前，则取A区块最后一条交易记录返回给客户端，所以first必定为false，只需要查看A区块后面是否有存在交易记录的区块来决定last的值
                        // 查询后一个有交易记录的块
                        blockExample = new BlockExample();
                        blockExample.createCriteria()
                                .andChainIdEqualTo(currTransaction.getChainId())
                                .andNumberGreaterThan(prevOrNextBlockNumber)
                                .andTransactionNumberGreaterThan(0);
                        // 取一条记录，避免影响性能
                        PageHelper.startPage(1,1);
                        List<Block> tmpBlocks = blockMapper.selectByExample(blockExample);
                        long blockCount = tmpBlocks.size();
                        if(blockCount==0){
                            // 如果后面不存在带有交易记录的块，则表示是最后一条交易记录
                            transactionDetailNavigate.setLast(true);
                        }
                        break;
                    case NEXT:
                        // 向后，则取A区块第一条交易记录返回给客户端，所以last必定为false，只需要查看A区块前面是否有存在交易记录的区块来决定first的值
                        // 查询前一个有交易记录的块
                        blockExample = new BlockExample();
                        blockExample.createCriteria()
                                .andChainIdEqualTo(currTransaction.getChainId())
                                .andNumberLessThan(prevOrNextBlockNumber)
                                .andTransactionNumberGreaterThan(0);
                        // 取一条记录，避免影响性能
                        PageHelper.startPage(1,1);
                        tmpBlocks = blockMapper.selectByExample(blockExample);
                        blockCount = tmpBlocks.size();
                        if(blockCount==0){
                            // 如果前面不存在带有交易记录的块，则表示是第一条交易记录
                            transactionDetailNavigate.setFirst(true);
                        }
                        break;
                }
            }

            /** 第3步、根据nextBlockNumber获取区块中的交易记录：如果是向前浏览，则取最后一条交易记录，如果是向后浏览则取第一条交易记录 **/
            condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(currTransaction.getChainId()).andBlockNumberEqualTo(prevOrNextBlockNumber);
            switch (direction){
                case PREV:
                    // 上一条，则拿上一个有交易记录的块的最后一条交易
                    condition.setOrderByClause("transaction_index desc");
                    break;
                case NEXT:
                    // 下一条，则取下一个有交易记录的块的第一条交易
                    condition.setOrderByClause("transaction_index asc");
                    break;
            }
            PageHelper.startPage(1,1);
            transactionList = transactionMapper.selectByExampleWithBLOBs(condition);
            if(transactionList.size()==0){
                logger.error("no transaction found in block: {}",prevOrNextBlockNumber);
                throw new BusinessException(RetEnum.RET_FAIL.getCode(), TransactionErrorEnum.NOT_EXIST.desc);
            }
            TransactionWithBLOBs transaction = transactionList.get(0);
            BeanUtils.copyProperties(transaction,transactionDetailNavigate);
            transactionDetailNavigate.setTxHash(transaction.getHash());
            transactionDetailNavigate.setBlockHeight(transaction.getBlockNumber());
            transactionDetailNavigate.setInputData(transaction.getInput());
            transactionDetailNavigate.setTimestamp(transaction.getTimestamp().getTime());
        }

        // 取最高区块，用于计算区块确认数: 区块确认数 = 链上最高区块号-当前区块号
        BlockExample blockExample = new BlockExample();
        blockExample.setOrderByClause("number desc");
        PageHelper.startPage(1,1);
        List<Block> blockList = blockMapper.selectByExample(blockExample);
        if(blockList.size()==0){
            transactionDetailNavigate.setConfirmNum(0l);
            return transactionDetailNavigate;
        }
        Block topBlock = blockList.get(0);
        transactionDetailNavigate.setConfirmNum(topBlock.getNumber()-transactionDetailNavigate.getBlockHeight());

        return transactionDetailNavigate;
    }
}
