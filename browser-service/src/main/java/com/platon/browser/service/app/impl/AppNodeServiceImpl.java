package com.platon.browser.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.app.node.AppNodeDetailDto;
import com.platon.browser.dto.app.node.AppNodeDto;
import com.platon.browser.dto.app.node.AppNodeListWrapper;
import com.platon.browser.dto.app.node.AppNodeVoteSummaryDto;
import com.platon.browser.service.ApiService;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.app.AppNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.platon.contracts.TicketContract;

import java.util.ArrayList;
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
    public AppNodeListWrapper list(String chainId) throws Exception {
        logger.debug("list() begin");
        long beginTime = System.currentTimeMillis();
        List<AppNodeDto> returnData = customNodeRankingMapper.selectByChainIdAndIsValidOrderByRanking(chainId,1);
        logger.debug("list() Time Consuming: {}ms",System.currentTimeMillis()-beginTime);
        AppNodeListWrapper nodes = new AppNodeListWrapper();
        nodes.setList(returnData);

        TicketContract ticketContract = platon.getTicketContract(chainId);
        String price = ticketContract.GetTicketPrice().send();
        nodes.setTicketPrice(price);

        // 从链上查投票数
        StringBuilder ids = new StringBuilder();
        List<String> nodeIds = new ArrayList<>();
        returnData.forEach(node->{
            ids.append(node.getNodeId()).append(":");
            nodeIds.add(node.getNodeId());
        });
        String idsStr = ids.toString();
        idsStr = idsStr.substring(0,idsStr.lastIndexOf(":"));
        String countInfo = ticketContract.GetCandidateTicketCount(idsStr).send();
        Map<String,Integer> countMap = JSON.parseObject(countInfo, Map.class);
        countMap.forEach((k,v)->nodes.setVoteCount(nodes.getVoteCount()+v));
        // 从交易表中查询总投票数量
        long totalVoteCount = customNodeRankingMapper.getVoteCountByNodeIds(chainId,nodeIds);
        nodes.setTotalCount(totalVoteCount);


        return nodes;
    }

    @Override
    public AppNodeDetailDto detail(String chainId, String nodeId) {
        AppNodeDetailDto node = customNodeRankingMapper.detailByChainIdAndNodeId(chainId,nodeId);
        return node;
    }


}
