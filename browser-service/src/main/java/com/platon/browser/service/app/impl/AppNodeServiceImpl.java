package com.platon.browser.service.app.impl;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.transaction.AppTransactionDto;
import com.platon.browser.dto.app.transaction.AppVoteTransactionDto;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.req.app.AppTransactionListVoteReq;
import com.platon.browser.service.ApiService;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.app.AppNodeService;
import com.platon.browser.service.app.AppTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:42
 */
@Service
public class AppNodeServiceImpl implements AppNodeService {

    private final Logger logger = LoggerFactory.getLogger(AppNodeServiceImpl.class);
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
    private CustomNodeRankingMapper customNodeRankingMapper;

    @Autowired
    private ApiService apiService;

    @Override
    public List<AppNodeDto> list(String chainId) {
        logger.debug("list() begin");
        long beginTime = System.currentTimeMillis();
        List<AppNodeDto> returnData = customNodeRankingMapper.selectByChainIdAndIsValidOrderByRanking(chainId,1);
        logger.debug("list() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return returnData;
    }

    @Override
    public AppNodeDetailDto detail(String chainId, String nodeId) {
        AppNodeDetailDto node = customNodeRankingMapper.detailByChainIdAndNodeId(chainId,nodeId);
        return node;
    }


}
