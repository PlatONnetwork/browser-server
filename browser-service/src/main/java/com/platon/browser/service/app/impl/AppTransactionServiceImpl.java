package com.platon.browser.service.app.impl;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.app.transaction.TransactionDto;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.app.AppTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:42
 */
@Service
public class AppTransactionServiceImpl implements AppTransactionService {

    private final Logger logger = LoggerFactory.getLogger(AppTransactionServiceImpl.class);
    @Autowired
    private PlatonClient platon;
    @Autowired
    private VoteTxMapper txMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private CustomTransactionMapper customTransactionMapper;

    @Override
    public List<TransactionDto> list(String chainId, AppTransactionListReq req) {
        logger.debug("transactionList begin");
        long beginTime = System.currentTimeMillis();
        List<TransactionDto> returnData = customTransactionMapper.selectByChainIdAndAddressAndBeginSequence(chainId,req.getAddress(),req.getBeginSequence(),req.getListSize());
        logger.debug("transactionList Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return returnData;
    }

}
