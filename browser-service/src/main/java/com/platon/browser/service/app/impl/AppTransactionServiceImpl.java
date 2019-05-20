package com.platon.browser.service.app.impl;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.CustomTransactionMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.app.transaction.AppTransactionDto;
import com.platon.browser.dto.app.transaction.AppVoteTransactionDto;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.enums.app.DirectionEnum;
import com.platon.browser.req.app.AppTransactionListReq;
import com.platon.browser.req.app.AppTransactionListVoteReq;
import com.platon.browser.service.ApiService;
import com.platon.browser.service.app.AppTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:42
 */
@Service
public class AppTransactionServiceImpl implements AppTransactionService {

    private final Logger logger = LoggerFactory.getLogger(AppTransactionServiceImpl.class);

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    @Autowired
    private CustomTransactionMapper customTransactionMapper;

    @Autowired
    private ApiService apiService;

    @Override
    public List<AppTransactionDto> list(String chainId, AppTransactionListReq req) {
        logger.debug("list() begin");
        List<AppTransactionDto> returnData;
        try {
            DirectionEnum directionEnum = DirectionEnum.valueOf(req.getDirection().toUpperCase());

            long beginTime = System.currentTimeMillis();
            returnData = customTransactionMapper.selectByChainIdAndAddressAndBeginSequenceAndDirection(
                    chainId,
                    req.getWalletAddrs(),
                    req.getBeginSequence(),
                    req.getDirection(),
                    req.getListSize()
            );

            if(DirectionEnum.NEW==directionEnum){
                // 取新记录，因为数据库中是顺排的，所以需要倒排
                Collections.sort(returnData,(c1,c2)->{
                    long s1=Long.valueOf(c1.getSequence()),s2=Long.valueOf(c2.getSequence());
                    if(s1>s2) return -1;
                    if(s1<s2) return 1;
                    return 0;
                });
            }
            logger.debug("list() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return returnData;
    }

    @Override
    public List<AppVoteTransactionDto> listVote(String chainId, AppTransactionListVoteReq req) {
        logger.debug("listVote() begin");
        long beginTime = System.currentTimeMillis();
        List<AppVoteTransactionDto> returnData = customTransactionMapper.selectByChainIdAndTxTypeAndNodeIdAndAddressesAndBeginSequence(
                chainId,
                TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code,
                req.getNodeId(),
                req.getWalletAddrs(),
                req.getBeginSequence(),
                req.getListSize());


        if(returnData.size()>0){
            List<String> hashList = new ArrayList<>();
            List<String> nodeIds = new ArrayList<>();
            returnData.forEach(voteTransactionDto -> {
                hashList.add(voteTransactionDto.getHash());
                nodeIds.add(voteTransactionDto.getNodeId());
            });

            // 取节点名称
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId)
                    .andNodeIdEqualTo(req.getNodeId());
            List<NodeRanking> nodes = nodeRankingMapper.selectByExample(nodeRankingExample);
            Map<String,String> nodeIdToNodeNameMap = new HashMap<>();
            nodes.forEach(node->nodeIdToNodeNameMap.put(node.getNodeId(),node.getName()));

            Map<String, BigDecimal> incomeMap = apiService.getIncome(chainId,hashList);
            Map<String,Integer> validVoteMap = apiService.getVailInfo(hashList, chainId);

            returnData.forEach(voteTransaction->{
                // 设置收益
                BigDecimal inCome = incomeMap.get(voteTransaction.getHash());
                if(null == inCome) voteTransaction.setEarnings(BigDecimal.ZERO.toString());
                else voteTransaction.setEarnings(inCome.toString());
                // 设置交易有效票数
                Integer validSum = validVoteMap.get(voteTransaction.getHash());
                voteTransaction.setValidNum(String.valueOf(validSum));
                // 设置节点名称
                String nodeName = nodeIdToNodeNameMap.get(voteTransaction.getNodeId());
                if(nodeName!=null) voteTransaction.setName(nodeName);
                // 设置锁定金额
                String lock = new BigDecimal(validSum).multiply(new BigDecimal(voteTransaction.getPrice())).toString();
                voteTransaction.setLocked(lock);
                // 设置预计过期时间，出块时间+26天
                BigInteger diff = BigInteger.valueOf(26*24*60).multiply(BigInteger.valueOf(60*1000));
                BigInteger deadLine = new BigInteger(voteTransaction.getTransactionTime()).add(diff);
                voteTransaction.setDeadLine(deadLine.toString());
            });
        }
        logger.debug("listVote() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        return returnData;
    }

}
