package com.platon.browser.service.impl;

import com.github.pagehelper.PageHelper;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.search.SearchResult;
import com.platon.browser.req.search.SearchReq;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.transaction.AccTransactionItem;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.service.*;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缓存服务
 * 提供首页节点信息、统计信息、区块信息、交易信息
 */
@Service
public class SearchServiceImpl implements SearchService {

    private final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PendingTxService pendingTxService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private I18nUtil i18n;

    @Override
    public SearchResult<?> search (SearchReq param ) {
        //以太坊内部和外部账户都是20个字节，0x开头，string长度40,加上0x，【外部账户-钱包地址，内部账户-合约地址】
        //以太坊区块hash和交易hash都是0x打头长度33
        //1.判断是否是块高
        //2.判断是否是地址
        //3.不是以上两种情况，就为交易hash或者区块hash，需要都查询
        param.setParameter(param.getParameter().trim());
        String chainId = param.getCid();
        String keyword = param.getParameter();
        boolean isAccountOrContract = false, isTransactionOrBlock = false, isNodePublicKey=false;
        boolean isNumber=keyword.matches("[0-9]+");
        if (!isNumber) {
            //为false则可能为区块交易hash或者为账户
            if(keyword.length()<=2)
                throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_TOO_SHORT));
            if("0x".equals(keyword.substring(0, 2))){
                if(keyword.length() == 42)
                    isAccountOrContract = true;
                if(keyword.length() == 130)
                    isNodePublicKey = true;
            } else {
                isTransactionOrBlock = true;
            }
        }

        SearchResult<Object> result = new SearchResult<>();

        if (isAccountOrContract) {
            // 账户或合约
            AccountDetailReq accountDetailReq = new AccountDetailReq();
            accountDetailReq.setCid(chainId);
            accountDetailReq.setAddress(keyword);
            /**
             * 逻辑分析：
             * 1、对同一条链，钱包地址和合约地址是属性同一命名空间的，因此是唯一的，钱包地址和合约地址不可能相同；
             * 2、由于from字段的值必定是钱包地址，所以首先查看from里是否有查询关键字的值，有则证明是钱包地址；
             * 3、没有则查看to字段里是否有查询关键字的值，如果to不存在查询关键字的值，证明查询无结果；
             *    如果to存在查询关键字的值，由于to既可存放钱包地址也可存放合约地址，所以需要根据receive_type字段的值来判定to存放的是钱包地址还是合约地址。
             *
             *    TO-DO 如果查询的地址只存在于pending表中，则这个地址会查不到
             */
            // 查询交易表的from字段是否有查询关键字的值
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId)
                    .andFromEqualTo(keyword);
            long transactionCount = transactionMapper.countByExample(condition);
            if(transactionCount>0){
                // from里是否有查询关键字的值, 证明是钱包地址【外部账户】
                List<AccTransactionItem> trades = accountService.getTransactionList(accountDetailReq);
                AddressDetail detail = new AddressDetail();
                detail.setTrades(trades);
                result.setStruct(detail);
                result.setType("account");
                return result;
            }
            if(transactionCount==0){
                // from里是否有查询关键字的值, 需要进一步查询to字段是否有查询关键字的值
                condition = new TransactionExample();
                condition.createCriteria().andChainIdEqualTo(chainId)
                        .andToEqualTo(keyword);
                // 为提高查询性能，只取一条数据
                PageHelper.startPage(1,1);
                List<Transaction> transactionList = transactionMapper.selectByExample(condition);
                if(transactionList.size()==0){
                    // to不存在查询关键字的值，证明查询无结果，直接返回
                    return result;
                }
                // to存在查询关键字的值, 取出此记录，查看其receive_type字段的值来判定to字段存放的是钱包地址还是合约地址
                Transaction transaction = transactionList.get(0);
                List<AccTransactionItem> trades = accountService.getTransactionList(accountDetailReq);
                result.setType(transaction.getReceiveType());
                if("account".equals(transaction.getReceiveType())){
                    // 存放的是账户地址
                    AddressDetail detail = new AddressDetail();
                    detail.setTrades(trades);
                    result.setStruct(detail);
                    return result;
                }
                if("contract".equals(transaction.getReceiveType())){
                    // 存放的是合约地址
                    ContractDetail detail = new ContractDetail();
                    detail.setTrades(trades);
                    result.setStruct(detail);
                    return result;
                }
            }
            return result;
        }

        if (isTransactionOrBlock) {
            // 交易hash或者区块hash
            /**
             * 逻辑分析
             * 1、优先查询已完成交易
             * 2、已完成交易查询无记录，则查询区块
             * 3、区块查询无记录，则查询待处理交易
             * 4、以上都无记录，则返回空结果
             */
            TransactionDetailReq transactionDetailReq = new TransactionDetailReq();
            transactionDetailReq.setCid(chainId);
            transactionDetailReq.setTxHash(keyword);
            try{
                // 此处调用如果查询不到交易记录会抛出BusinessException异常
                TransactionDetail transactionDetail = transactionService.getTransactionDetail(transactionDetailReq);
                result.setStruct(transactionDetail);
                result.setType("transaction");
                return result;
            }catch (BusinessException be){
                logger.info("在交易表查询不到Hash为[{}]的交易记录，尝试查询Hash为[{}]的区块信息...",keyword,keyword);
                BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(chainId).andHashEqualTo(keyword);
                List<Block> blockList = blockMapper.selectByExample(blockExample);
                if(blockList.size()>0){
                    // 如果找到区块信息，则构造结果并返回
                    BlockDetail blockDetail = new BlockDetail();
                    Block block = blockList.get(0);
                    BeanUtils.copyProperties(block,blockDetail);
                    blockDetail.setHeight(block.getNumber());
                    blockDetail.setTimestamp(block.getTimestamp().getTime());
                    result.setType("block");
                    result.setStruct(blockDetail);
                    return result;
                }

                logger.info("在区块表查询不到Hash为[{}]的区块信息，尝试查询Hash为[{}]的待处理交易记录...",keyword,keyword);
                PendingTxDetailReq pendingTxDetailReq = new PendingTxDetailReq();
                pendingTxDetailReq.setCid(chainId);
                pendingTxDetailReq.setTxHash(keyword);
                try{
                    PendingOrTransaction pendingOrTransaction = pendingTxService.getTransactionDetail(pendingTxDetailReq);
                    result.setType(pendingOrTransaction.getType());
                    result.setStruct(pendingOrTransaction.getPending());
                    return result;
                }catch (BusinessException be2){
                    throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_NO_RESULT));
                }
            }
        }

        if(isNumber){
            // 如果查询关键字是纯数字，则查询区块信息返回
            BlockDetailReq req = new BlockDetailReq();
            req.setCid(chainId);
            req.setHeight(Long.valueOf(keyword));
            try{
                BlockDetail blockDetail = blockService.getBlockDetail(req);
                result.setStruct(blockDetail);
                result.setType("block");
                return result;
            }catch (BusinessException be){
                throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_NO_RESULT));
            }
        }

        throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_NO_RESULT));
    }
}
